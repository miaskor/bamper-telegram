package by.miaskor.bot.service.handler.state

import by.miaskor.bot.domain.BotState
import reactor.core.publisher.Mono

class BotStateHandlerRegistry(
  vararg handlers: BotStateHandler
) {

  private val mapHandlers = handlers.associateBy { it.state }

  fun lookup(botState: BotState): Mono<BotStateHandler> {
    return Mono.just(
      mapHandlers[botState]
        ?: throw IllegalStateException("State bot = $botState doesn't exists")
    )
  }
}
