package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.CreatingCarMessageSettings
import by.miaskor.bot.domain.BotState.CREATING_CAR
import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.Command.NEXT_STEP
import by.miaskor.bot.domain.Command.PREVIOUS_STEP
import by.miaskor.bot.domain.CreatingCarStep.BODY
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.Companion.stepsWithMovementForward
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_CAPACITY
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_TYPE
import by.miaskor.bot.domain.CreatingCarStep.FUEL_TYPE
import by.miaskor.bot.domain.CreatingCarStep.MODEL
import by.miaskor.bot.domain.CreatingCarStep.TRANSMISSION
import by.miaskor.bot.domain.CreatingCarStep.YEAR
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.pollLast
import by.miaskor.bot.service.text
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
        if (NEXT_STEP.isCommand(update.text) && carBuilder.currentStep() == ENGINE_TYPE) {
          completeCreatingCar(update, creatingCarMessage)
        } else if (NEXT_STEP.isCommand(update.text) &&
          stepsWithMovementForward.contains(carBuilder.currentStep())
        ) {
          val nextStep = carBuilder.nextStep()
          populateCache(update, nextStep)
          sendMessage(nextStep, update, creatingCarMessage)
        } else if (PREVIOUS_STEP.isCommand(update.text)) {
          val previousStep = carBuilder.previousStep()
          populateCache(update, previousStep)
          sendMessage(previousStep, update, creatingCarMessage)
        } else {
          processStep(carBuilder, update, creatingCarMessage)
        }
      }
  }

  private fun completeCreatingCar(
    update: Update,
    creatingCarMessage: CreatingCarMessageSettings
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { completeCreatingCar(update, creatingCarMessage, it) }
  }

  private fun completeCreatingCar(
    update: Update,
    creatingCarMessage: CreatingCarMessageSettings,
    telegramClient: TelegramClient
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, telegramClient.previousBotStates.pollLast())
      .flatMap(keyboardBuilder::build)
      .map {
        telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.completeCreatingMessage(), it)
        populateCache(update)
      }.then(Mono.empty())
  }

  private fun sendMessage(
    carBuilder: CarBuilder,
    update: Update,
    creatingCarMessage: CreatingCarMessageSettings
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(keyboardBuilder::build)
      .map {
        when (carBuilder.currentStep()) {
          BRAND_NAME -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.brandNameMessage(), keyboard)
          }
          MODEL -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.modelMessage(), keyboard)
          }
          YEAR -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.yearMessage(), keyboard)
          }
          BODY -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.bodyMessage(), keyboard)
          }
          TRANSMISSION -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.transmissionMessage(), keyboard)
          }
          ENGINE_CAPACITY -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.engineCapacityMessage(), keyboard)
          }
          FUEL_TYPE -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.fuelTypeMessage(), keyboard)
          }
          ENGINE_TYPE -> {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.engineTypeMessage(), keyboard)
          }
        }
      }
  }

  private fun processStep(
    carBuilder: CarBuilder,
    update: Update,
    creatingCarMessage: CreatingCarMessageSettings
  ): Mono<Unit> {
    return Mono.fromSupplier {
      when (carBuilder.currentStep()) {
        BRAND_NAME -> {
          if (BRAND_NAME.isAcceptable(update.text)) {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForMandatorySteps())
            populateCache(update, carBuilder.brandName(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.modelMessage(), keyboard)
          } else {
            telegramBot.sendMessage(update.chatId, creatingCarMessage.invalidBrandNameMessage())
          }
        }
        MODEL -> {
          if (MODEL.isAcceptable(update.text)) {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForMandatorySteps())
            populateCache(update, carBuilder.model(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.yearMessage(), keyboard)
          } else {
            telegramBot.sendMessage(update.chatId, creatingCarMessage.invalidModelMessage())
          }
        }
        YEAR -> {
          if (YEAR.isAcceptable(update.text)) {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
            populateCache(update, carBuilder.year(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.bodyMessage(), keyboard)
          } else {
            telegramBot.sendMessage(update.chatId, creatingCarMessage.invalidYearMessage())
          }
        }
        BODY -> {
          if (BODY.isAcceptable(update.text)) {
            val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
            populateCache(update, carBuilder.body(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.transmissionMessage(), keyboard)
          } else {
            telegramBot.sendMessage(update.chatId, creatingCarMessage.invalidBodyMessage())
          }
        }
        TRANSMISSION -> {
          val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
          if (TRANSMISSION.isAcceptable(update.text)) {
            populateCache(update, carBuilder.transmission(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.engineCapacityMessage(), keyboard)
          } else {
            telegramBot.sendMessageWithKeyboard(
              update.chatId,
              creatingCarMessage.invalidTransmissionMessage(),
              keyboard
            )
          }
        }
        ENGINE_CAPACITY -> {
          val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
          if (ENGINE_CAPACITY.isAcceptable(update.text)) {
            populateCache(update, carBuilder.engineCapacity(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.fuelTypeMessage(), keyboard)
          } else {
            telegramBot.sendMessageWithKeyboard(
              update.chatId,
              creatingCarMessage.invalidEngineCapacityMessage(),
              keyboard
            )
          }
        }
        FUEL_TYPE -> {
          val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
          if (FUEL_TYPE.isAcceptable(update.text)) {
            populateCache(update, carBuilder.fuelType(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.engineTypeMessage(), keyboard)
          } else {
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.invalidFuelTypeMessage(), keyboard)
          }
        }
        ENGINE_TYPE -> {
          val keyboard = keyboardBuilder.buildKeyboard(creatingCarMessage.keyboardForNotMandatorySteps())
          if (ENGINE_TYPE.isAcceptable(update.text)) {
            populateCache(update, carBuilder.engineType(update.text).nextStep())
          } else {
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingCarMessage.invalidEngineTypeMessage(), keyboard)
          }
        }
      }
    }.filter { carBuilder.currentStep() == ENGINE_TYPE }
      .flatMap { completeCreatingCar(update, creatingCarMessage) }
  }

  private fun populateCache(update: Update, carBuilder: CarBuilder = CarBuilder()): CarBuilder {
    stepCache.put(update.chatId, carBuilder)
    return carBuilder
  }
}
