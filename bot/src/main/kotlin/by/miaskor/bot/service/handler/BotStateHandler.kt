package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.BotState
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

interface BotStateHandler {
  val state: BotState
  fun handle(update: Update): Mono<Unit>
}
