package by.miaskor.bot.service.handler.state

import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.service.CommandResolver
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class EmployeesMenuHandler(
  private val commandResolver: CommandResolver
) : BotStateHandler {
  override val state = EMPLOYEES_MENU

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap { commandResolver.resolve(it, state) }
      .then(Mono.empty())
  }
}
