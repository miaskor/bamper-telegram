package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.BotState.CREATING_CAR
import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.MessageSender.sendMessageWithKeyboard
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.pollLast
import by.miaskor.bot.service.step.ProcessingStepService
import by.miaskor.bot.service.step.car.CreatingCarStepKeyboardBuilder
import by.miaskor.bot.service.step.car.CreatingCarStepMessageResolver
import by.miaskor.domain.api.connector.CarConnector
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CreatingCarHandler(
  creatingCarStepCache: Cache<Long, AbstractStepBuilder<CreatingCarStep>>,
  creatingCarStepKeyboardBuilder: CreatingCarStepKeyboardBuilder,
  creatingCarStepMessageResolver: CreatingCarStepMessageResolver,
  telegramBot: TelegramBot,
  processingStepService: ProcessingStepService<CreatingCarStep>,
  private val telegramClientCache: TelegramClientCache,
  private val carConnector: CarConnector
) : BotStateHandler, StepHandler<CreatingCarStep>(
  creatingCarStepCache,
  processingStepService,
  creatingCarStepKeyboardBuilder,
  creatingCarStepMessageResolver,
  telegramBot
) {
  override val state = CREATING_CAR

  override fun handle(update: Update): Mono<Unit> {
    return handle(update, CarBuilder())
  }

  override fun completeStep(
    update: Update,
    stepBuilder: AbstractStepBuilder<CreatingCarStep>
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { completeCreatingCar(update, it, stepBuilder as CarBuilder) }
  }

  private fun completeCreatingCar(
    update: Update,
    telegramClient: TelegramClient,
    carStepBuilder: CarBuilder
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, telegramClient.previousBotStates.pollLast())
      .flatMap { carConnector.create(carStepBuilder.build(telegramClient.currentStoreHouseId())) }
      .map { carId ->
        sendMessageWithKeyboard<Long>(update.chatId, MessageSettings::completeCreatingCarMessage, carId.toString())
      }
      .then(Mono.empty())
  }
}
