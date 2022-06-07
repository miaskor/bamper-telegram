package by.miaskor.bot.service.handler.state

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART
import by.miaskor.bot.domain.FindAutoPartBuilder
import by.miaskor.bot.domain.FindingAutoPartStep
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.step.ProcessingStepService
import by.miaskor.bot.service.step.autopart.find.FindingAutoPartStepKeyboardBuilder
import by.miaskor.bot.service.step.autopart.find.FindingAutoPartStepMessageResolver
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class FindingAutoPartHandler(
  creatingCarStepCache: Cache<Long, AbstractStepBuilder<FindingAutoPartStep>>,
  findingAutoPartStepKeyboardBuilder: FindingAutoPartStepKeyboardBuilder,
  FindingAutoPartStepMessageResolver: FindingAutoPartStepMessageResolver,
  telegramBot: TelegramBot,
  processingStepService: ProcessingStepService<FindingAutoPartStep>
) : BotStateHandler, StepHandler<FindingAutoPartStep>(
  creatingCarStepCache,
  processingStepService,
  findingAutoPartStepKeyboardBuilder,
  FindingAutoPartStepMessageResolver,
  telegramBot
) {
  override val state = FINDING_AUTO_PART

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap { handle(update, FindAutoPartBuilder()) }
  }

  override fun completeStep(update: Update, stepBuilder: AbstractStepBuilder<FindingAutoPartStep>): Mono<Unit> {
    TODO("Not yet implemented")
  }
}