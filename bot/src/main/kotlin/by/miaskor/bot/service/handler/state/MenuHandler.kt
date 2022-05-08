package by.miaskor.bot.service.handler.state

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.CommandResolver
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class MenuHandler(
  override val state: BotState,
  private val commandResolver: CommandResolver
) : BotStateHandler {
  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap { commandResolver.resolve(it, state) }
      .then(Mono.empty())
  }
}
