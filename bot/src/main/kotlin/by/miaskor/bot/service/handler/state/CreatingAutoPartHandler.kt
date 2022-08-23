package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.AutoPartBuilder
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.MessageSender.sendMessageWithKeyboard
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.pollLast
import by.miaskor.bot.service.step.ProcessingStepService
import by.miaskor.bot.service.step.autopart.create.CreatingAutoPartStepKeyboardBuilder
import by.miaskor.bot.service.step.autopart.create.CreatingAutoPartStepMessageResolver
import by.miaskor.domain.api.connector.AutoPartConnector
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CreatingAutoPartHandler(
  creatingCarStepCache: Cache<Long, AbstractStepBuilder<CreatingAutoPartStep>>,
  creatingAutoPartStepKeyboardBuilder: CreatingAutoPartStepKeyboardBuilder,
  creatingAutoPartStepMessageResolver: CreatingAutoPartStepMessageResolver,
  telegramBot: TelegramBot,
  processingStepService: ProcessingStepService<CreatingAutoPartStep>,
  private val telegramClientCache: TelegramClientCache,
  private val autoPartConnector: AutoPartConnector,
) : BotStateHandler, StepHandler<CreatingAutoPartStep>(
  creatingCarStepCache,
  processingStepService,
  creatingAutoPartStepKeyboardBuilder,
  creatingAutoPartStepMessageResolver,
  telegramBot
) {
  override val state = BotState.CREATING_AUTO_PART

  override fun handle(update: Update): Mono<Unit> {
    return handle(update, AutoPartBuilder())
  }

  override fun completeStep(
    update: Update,
    stepBuilder: AbstractStepBuilder<CreatingAutoPartStep>
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { completeCreatingAutoPart(update, it, stepBuilder as AutoPartBuilder) }
  }

  private fun completeCreatingAutoPart(
    update: Update,
    telegramClient: TelegramClient,
    autoPartBuilder: AutoPartBuilder,
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, telegramClient.previousBotStates.pollLast())
      .flatMap {
        sendMessageWithKeyboard<Unit>(it, MessageSettings::completeCreatingAutoPartMessage).thenReturn(it)
      }.map {
        autoPartConnector.create(autoPartBuilder.build(telegramClient.currentStoreHouseId(), update.chatId))
          .toFuture()
          .completeAsync {}
      }
  }
}
