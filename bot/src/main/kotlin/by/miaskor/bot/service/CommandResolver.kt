package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

object CommandResolver {

  lateinit var commandHandlerRegistry: CommandHandlerRegistry
  lateinit var botStateHandlerRegistry: BotStateHandlerRegistry
  lateinit var callBackCommandHandler: CallBackCommandHandler

  fun Mono<BotState>.processCommand(update: Update): Mono<Unit> {
    return this.flatMap<Unit> { t ->
      Mono.just(update)
        .filter { it.callbackQuery() != null }
        .switchIfEmpty(
          Mono.from(t.getCommand(update.text))
            .flatMap(commandHandlerRegistry::lookup)
            .switchIfEmpty(
              Mono.just(t)
                .flatMap(botStateHandlerRegistry::lookup)
                .flatMap { it.handle(update) }
                .then(Mono.empty())
            ).flatMap { it.handle(update) }
            .then(Mono.empty())
        )
        .flatMap(callBackCommandHandler::handle)
        .then(Mono.empty())
    }.subscribeOn(Schedulers.boundedElastic())
  }
}
