package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command.UNDEFINED
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

object CommandResolver {

  lateinit var commandHandlerRegistry: CommandHandlerRegistry
  lateinit var botStateHandlerRegistry: BotStateHandlerRegistry

  fun Mono<BotState>.processCommand(update: Update): Mono<Unit> {
    return this.flatMap { t ->
      Mono.from(t.getCommand(update.text))
        .switchIfEmpty(
          Mono.just(t)
            .flatMap(botStateHandlerRegistry::lookup)
            .flatMap { it.handle(update) }
            .then(Mono.empty())
        )
        .flatMap(commandHandlerRegistry::lookup)
        .flatMap { it.handle(update) }
    }
  }
}
