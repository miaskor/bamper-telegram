package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.service.handler.CommandHandlerRegistry
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CommandResolver(
  private val commandHandlerRegistry: CommandHandlerRegistry
) {

  fun resolve(update: Update, botState: BotState): Mono<Unit> {
    if (botState == MAIN_MENU) {
      if (CHANGE_LANGUAGE.isCommand(update.text)) {
        return commandHandlerRegistry.lookup(CHANGE_LANGUAGE)
          .flatMap { it.handle(update) }
      }
    }
    return Mono.empty()
  }
}
