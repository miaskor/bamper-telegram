package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.CreatingCarMessageSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.BotState.CREATING_CAR
import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.Command.NEXT_STEP
import by.miaskor.bot.domain.Command.PREVIOUS_STEP
import by.miaskor.bot.domain.CreatingCarStep.Companion.stepsWithMovementForward
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_TYPE
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.carstep.CreatingCarStepMessageResolver
import by.miaskor.bot.service.carstep.ProcessingStepService
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.pollLast
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.CarConnector
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.time.Duration

class CreatingCarHandler(
  private val cacheSettings: CacheSettings,
  private val telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache,
  private val keyboardBuilder: KeyboardBuilder,
  private val processingStepService: ProcessingStepService,
  private val carConnector: CarConnector
) : BotStateHandler {
  override val state = CREATING_CAR

  private val stepCache: Cache<Long, CarBuilder> = Caffeine.newBuilder()
    .expireAfterWrite(Duration.parse(cacheSettings.entityTtl()))
    .maximumSize(cacheSettings.size())
    .build()

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(CreatingCarMessageSettings::class)
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, creatingCarMessageSettings: CreatingCarMessageSettings): Mono<Unit> {
    return Mono.justOrEmpty(stepCache.getIfPresent(update.chatId))
      .switchIfEmpty(Mono.fromSupplier { populateCache(update) })
      .zipWith(Mono.just(creatingCarMessageSettings))
      .flatMap { (carBuilder, creatingCarMessage) ->
        if (NEXT_STEP isCommand update.text && carBuilder.currentStep() == ENGINE_TYPE) {
          completeCreatingCar(update, creatingCarMessage, carBuilder)
        } else if (NEXT_STEP isCommand update.text &&
          stepsWithMovementForward.contains(carBuilder.currentStep())
        ) {
          val nextStep = carBuilder.nextStep()
          populateCache(update, nextStep)
          sendMessage(nextStep, update)
        } else if (PREVIOUS_STEP isCommand update.text) {
          val previousStep = carBuilder.previousStep()
          populateCache(update, previousStep)
          sendMessage(previousStep, update)
        } else {
          processStep(carBuilder, update, creatingCarMessage)
        }
      }
  }

  private fun completeCreatingCar(
    update: Update,
    creatingCarMessage: CreatingCarMessageSettings,
    carBuilder: CarBuilder
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { completeCreatingCar(update, creatingCarMessage, it, carBuilder) }
  }

  private fun completeCreatingCar(
    update: Update,
    creatingCarMessage: CreatingCarMessageSettings,
    telegramClient: TelegramClient,
    carBuilder: CarBuilder
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, telegramClient.previousBotStates.pollLast())
      .flatMap(keyboardBuilder::build)
      .zipWith(carConnector.create(carBuilder.build(telegramClient.currentStoreHouseId())))
      .map { (keyboard, carId) ->
        telegramBot.sendMessageWithKeyboard(
          update.chatId,
          creatingCarMessage.completeCreatingMessage().format(carId), keyboard
        )
        populateCache(update)
      }.then(Mono.empty())
  }

  private fun sendMessage(
    carBuilder: CarBuilder,
    update: Update
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(KeyboardSettings::class)
      .zipWith(CreatingCarStepMessageResolver.resolve(update, carBuilder.currentStep(), carBuilder, true))
      .map { (keyboardSettings, message) ->
        val keyboard = keyboardBuilder.buildCreatingCarStepKeyboard(carBuilder.currentStep(), keyboardSettings)
        telegramBot.sendMessageWithKeyboard(update.chatId, message, keyboard)
      }
  }

  private fun processStep(
    carBuilder: CarBuilder,
    update: Update,
    creatingCarMessage: CreatingCarMessageSettings
  ): Mono<Unit> {
    val currentStep = carBuilder.currentStep()
    return processingStepService.process(update, currentStep, carBuilder, this::populateCache)
      .filter { currentStep.isComplete }
      .flatMap { completeCreatingCar(update, creatingCarMessage, carBuilder) }
  }

  private fun populateCache(update: Update, carBuilder: CarBuilder = CarBuilder()): CarBuilder {
    stepCache.put(update.chatId, carBuilder)
    return carBuilder
  }
}
