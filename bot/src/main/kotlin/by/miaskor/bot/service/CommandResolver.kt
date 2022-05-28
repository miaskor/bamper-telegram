package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

object CommandResolver {

  lateinit var commandHandlerRegistry: CommandHandlerRegistry
  lateinit var botStateHandlerRegistry: BotStateHandlerRegistry
  lateinit var callBackQueryHandlerRegistry: CallBackQueryHandlerRegistry

  fun Mono<BotState>.processCommand(update: Update): Mono<Unit> {
    return this.flatMap { t ->
      Mono.from(t.getCommand(update.text))
        .flatMap(commandHandlerRegistry::lookup)
        .switchIfEmpty(
          Mono.just(update)
            .filter { it.callbackQuery() != null }
            .switchIfEmpty(
              Mono.just(t)
                .flatMap(botStateHandlerRegistry::lookup)
                .flatMap { it.handle(update) }
                .then(Mono.empty())
            )
            .flatMap(callBackQueryHandlerRegistry::handle)
            .then(Mono.empty())
        )
        .flatMap { it.handle(update) }

    }
  }
}
