package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command
import by.miaskor.bot.domain.Command.UNDEFINED
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CommandResolver(
  private val commandHandlerRegistry: CommandHandlerRegistry
) {

  fun resolve(update: Update, botState: BotState): Mono<Unit> {
    return Mono.from(botState.getCommand(update.text))
      .defaultIfEmpty(UNDEFINED)
      .flatMap(commandHandlerRegistry::lookup)
      .flatMap { it.handle(update) }
  }
}
