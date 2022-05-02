package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.StateBot
import reactor.core.publisher.Mono

class StateHandlerRegistry(
  vararg handlers: StateHandler
) {

  private val mapHandlers = handlers.associateBy { it.state }

  fun lookup(stateBot: StateBot): Mono<StateHandler> {
    return Mono.just(
      mapHandlers[stateBot]
        ?: throw IllegalStateException("State bot = $stateBot doesn't exists")
    )
  }
}
