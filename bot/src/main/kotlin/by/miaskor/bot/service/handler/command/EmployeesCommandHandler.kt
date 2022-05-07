package by.miaskor.bot.service.handler.command

import by.miaskor.bot.domain.Command.EMPLOYEES
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class EmployeesCommandHandler : CommandHandler {
  override val command = EMPLOYEES

  override fun handle(update: Update): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
