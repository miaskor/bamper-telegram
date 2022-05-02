package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.StateBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

interface StateHandler {
  val state: StateBot
  fun handle(update: Update): Mono<Unit>
}
