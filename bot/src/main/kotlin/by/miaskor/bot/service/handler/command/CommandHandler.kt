package by.miaskor.bot.service.handler.command

import by.miaskor.bot.domain.Command
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

interface CommandHandler {
  val command: Command
  fun handle(update: Update): Mono<Unit>
}
