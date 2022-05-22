package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.CreatingAutoPartMessageSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.AutoPartBuilder
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingAutoPartStep.AUTO_PART
import by.miaskor.bot.domain.CreatingAutoPartStep.CAR
import by.miaskor.bot.domain.CreatingAutoPartStep.CURRENCY
import by.miaskor.bot.domain.CreatingAutoPartStep.DESCRIPTION
import by.miaskor.bot.domain.CreatingAutoPartStep.PART_NUMBER
import by.miaskor.bot.domain.CreatingAutoPartStep.PHOTO
import by.miaskor.bot.domain.CreatingAutoPartStep.PRICE
import by.miaskor.bot.domain.CreatingAutoPartStep.QUALITY
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.photoId
import by.miaskor.bot.service.pollLast
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.connector.CarConnector
import by.miaskor.domain.api.connector.CarPartConnector
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.time.Duration

class CreatingAutoPartHandler(
  private val cacheSettings: CacheSettings,
  private val telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache,
  private val keyboardBuilder: KeyboardBuilder,
  private val autoPartConnector: AutoPartConnector,
  private val carConnector: CarConnector,
  private val carPartConnector: CarPartConnector,
) : BotStateHandler {
  override val state = BotState.CREATING_AUTO_PART

  private val stepCache: Cache<Long, AutoPartBuilder> = Caffeine.newBuilder()
    .expireAfterWrite(Duration.parse(cacheSettings.entityTtl()))
    .maximumSize(cacheSettings.size())
    .build()

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(CreatingAutoPartMessageSettings::class)
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, creatingAutoPartMessageSettings: CreatingAutoPartMessageSettings): Mono<Unit> {
    return Mono.justOrEmpty(stepCache.getIfPresent(update.chatId))
      .switchIfEmpty(Mono.fromSupplier { populateCache(update) })
      .zipWith(Mono.just(creatingAutoPartMessageSettings))
      .flatMap { (autoPartBuilder, creatingAutoPartMessage) ->
        if (Command.NEXT_STEP isCommand update.text &&
          CreatingAutoPartStep.stepsWithMovementForward.contains(autoPartBuilder.currentStep())
        ) {
          val nextStep = autoPartBuilder.nextStep()
          populateCache(update, nextStep)
          sendMessage(nextStep, update, creatingAutoPartMessage)
        } else if (Command.PREVIOUS_STEP isCommand update.text) {
          val previousStep = autoPartBuilder.previousStep()
          populateCache(update, previousStep)
          sendMessage(previousStep, update, creatingAutoPartMessage)
        } else {
          processStep(autoPartBuilder, update, creatingAutoPartMessage)
        }
      }
  }

  private fun completeCreatingCar(
    update: Update,
    creatingAutoPartMessageSettings: CreatingAutoPartMessageSettings,
    autoPartBuilder: AutoPartBuilder
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { completeCreatingCar(update, creatingAutoPartMessageSettings, it, autoPartBuilder) }
  }

  private fun completeCreatingCar(
    update: Update,
    creatingAutoPartMessageSettings: CreatingAutoPartMessageSettings,
    telegramClient: TelegramClient,
    autoPartBuilder: AutoPartBuilder
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, telegramClient.previousBotStates.pollLast())
      .flatMap(keyboardBuilder::build)
      .map { keyboard ->
        telegramBot.sendMessageWithKeyboard(
          update.chatId,
          creatingAutoPartMessageSettings.completeMessage(), keyboard
        )
        populateCache(update)
        keyboard
      }.map {
        autoPartConnector.create(autoPartBuilder.build(telegramClient.currentStoreHouseId(), update.chatId))
          .toFuture()
          .completeAsync {}
      }
  }

  private fun sendMessage(
    autoPartBuilder: AutoPartBuilder,
    update: Update,
    creatingAutoPartMessage: CreatingAutoPartMessageSettings
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(KeyboardSettings::class)
      .map { keyboardSettings ->
        when (autoPartBuilder.currentStep()) {
          CAR -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.carMessage(), keyboard)
          }

          AUTO_PART -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.autoPartMessage(), keyboard)
          }

          PART_NUMBER -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.partNumberMessage(), keyboard)
          }

          DESCRIPTION -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.descriptionMessage(), keyboard)
          }

          PRICE -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.priceMessage(), keyboard)
          }

          CURRENCY -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.currencyMessage(), keyboard)
          }

          QUALITY -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.qualityMessage(), keyboard)
          }

          PHOTO -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.photoMessage(), keyboard)
          }
        }
      }
  }

  private fun processStep(
    autoPartBuilder: AutoPartBuilder,
    update: Update,
    creatingAutoPartMessage: CreatingAutoPartMessageSettings
  ): Mono<Unit> {
    val currentStep = autoPartBuilder.currentStep()
    return Mono.just(update.chatId)
      .resolveLanguage(KeyboardSettings::class)
      .zipWith(telegramClientCache.getTelegramClient(update.chatId))
      .map { (keyboardSettings, telegramClient) ->
        when (currentStep) {
          CAR -> {
            val car = Mono.defer {
              carConnector.getByStoreHouseIdAndId(
                telegramClient.currentStoreHouseId(),
                update.text.toLong()
              ).hasElement()
            }
            if (CAR.isAcceptable(update.text) && car.toFuture().get()) {
              val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForMandatorySteps())
              populateCache(update, autoPartBuilder.carId(update.text.toLong()).nextStep())
              telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.autoPartMessage(), keyboard)
            } else {
              telegramBot.sendMessage(update.chatId, creatingAutoPartMessage.carInvalidMessage().format(update.text))
            }
          }

          AUTO_PART -> {
            if (!AUTO_PART.isAcceptable(update.text)) {
              telegramBot.sendMessage(
                update.chatId, creatingAutoPartMessage.autoPartInvalidMessage()
              )
            }
            val carPartId: Long? = carPartConnector.getIdByName(update.text).toFuture().get()
            if (carPartId != null) {
              val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
              populateCache(update, autoPartBuilder.carPartId(carPartId).nextStep())
              telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.partNumberMessage(), keyboard)
            } else {
              telegramBot.sendMessage(
                update.chatId, creatingAutoPartMessage.autoPartInvalidMessage()
              )
            }
          }

          PART_NUMBER -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            populateCache(update, autoPartBuilder.partNumber(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.descriptionMessage(), keyboard)
          }

          DESCRIPTION -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            populateCache(update, autoPartBuilder.description(update.text).nextStep())
            telegramBot.sendMessageWithKeyboard(
              update.chatId,
              creatingAutoPartMessage.priceMessage(),
              keyboard
            )
          }

          PRICE -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            if (PRICE.isAcceptable(update.text)) {
              populateCache(update, autoPartBuilder.price(update.text).nextStep())
              telegramBot.sendMessageWithKeyboard(
                update.chatId,
                creatingAutoPartMessage.currencyMessage(),
                keyboard
              )
            } else {
              telegramBot.sendMessageWithKeyboard(
                update.chatId,
                creatingAutoPartMessage.priceInvalidMessage(),
                keyboard
              )
            }
          }

          CURRENCY -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            if (CURRENCY.isAcceptable(update.text)) {
              populateCache(update, autoPartBuilder.currency(update.text).nextStep())
              telegramBot.sendMessageWithKeyboard(
                update.chatId,
                creatingAutoPartMessage.qualityMessage(),
                keyboard
              )
            } else {
              telegramBot.sendMessageWithKeyboard(
                update.chatId,
                creatingAutoPartMessage.currencyInvalidMessage(),
                keyboard
              )
            }
          }

          QUALITY -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            if (QUALITY.isAcceptable(update.text)) {
              populateCache(update, autoPartBuilder.quality(update.text).nextStep())
              telegramBot.sendMessageWithKeyboard(update.chatId, creatingAutoPartMessage.photoMessage(), keyboard)
            } else {
              telegramBot.sendMessageWithKeyboard(
                update.chatId,
                creatingAutoPartMessage.qualityInvalidMessage(),
                keyboard
              )
            }
          }

          PHOTO -> {
            val keyboard = keyboardBuilder.buildKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
            if (!update.message().photo().isNullOrEmpty()) {
              populateCache(update, autoPartBuilder.photoId(update.photoId).nextStep())
              completeCreatingCar(update, creatingAutoPartMessage, autoPartBuilder).subscribe()
            } else {
              telegramBot.sendMessageWithKeyboard(
                update.chatId,
                creatingAutoPartMessage.photoInvalidMessage(),
                keyboard
              )
            }
          }
        }
      }
  }

  private fun populateCache(update: Update, autoPartBuilder: AutoPartBuilder = AutoPartBuilder()): AutoPartBuilder {
    stepCache.put(update.chatId, autoPartBuilder)
    return autoPartBuilder
  }
}
