package by.miaskor.bot.service.handler.state

import by.miaskor.bot.domain.BotState.CHANGING_LANGUAGE
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ChangingLanguageHandler(
  private val choosingLanguageHandler: ChoosingLanguageHandler
) : BotStateHandler {
  override val state = CHANGING_LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap(choosingLanguageHandler::handle)
  }
}
