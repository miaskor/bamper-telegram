package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.Command
import reactor.core.publisher.Mono

class CommandHandlerRegistry(
  vararg handlers: CommandHandler
) {

  private val mapHandlers = handlers.associateBy { it.command }

  fun lookup(command: Command): Mono<CommandHandler> {
    return Mono.just(
      mapHandlers[command]
        ?: throw IllegalStateException("Command = $command doesn't exists")
    )
  }
}
