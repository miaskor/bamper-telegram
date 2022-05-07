package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.service.CommandResolver
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class MainMenuHandler(
  private val commandResolver: CommandResolver
) : BotStateHandler {
  override val state: BotState = MAIN_MENU

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap { commandResolver.resolve(it, state) }
      .then(Mono.empty())
  }
}
