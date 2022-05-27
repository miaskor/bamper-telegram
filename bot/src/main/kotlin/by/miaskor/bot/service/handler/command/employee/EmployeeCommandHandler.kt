package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command.EMPLOYEE
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class EmployeeCommandHandler(
  private val removingEmployeeCommandHandler: AbstractEmployeeCommandHandler,
  private val addingEmployeeCommandHandler: AbstractEmployeeCommandHandler,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .map { it.currentBotState }
      .flatMap {
        if (it == BotState.ADDING_EMPLOYEE) {
          addingEmployeeCommandHandler.handle(update)
        } else {
          removingEmployeeCommandHandler.handle(update)
        }
      }
  }
}
