package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command.EMPLOYEE
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class EmployeeCommandHandler(
  private val removingEmployeeCommandHandler: RemovingEmployeeCommandHandler,
  private val addingEmployeeCommandHandler: AddingEmployeeCommandHandler,
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
