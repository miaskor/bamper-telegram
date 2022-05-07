package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CommandResolver(
  private val commandHandlerRegistry: CommandHandlerRegistry
) {

  fun resolve(update: Update, botState: BotState): Mono<Unit> {
    if (botState == MAIN_MENU) {
      return Mono.fromSupplier { botState.getCommand(update.text) }
        .flatMap(commandHandlerRegistry::lookup)
        .flatMap { it.handle(update) }
    }
    return Mono.empty()
  }
}
