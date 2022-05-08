package by.miaskor.bot.service.handler.command

import by.miaskor.bot.domain.Command.LIST_EMPLOYEE
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.chatId
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class ListEmployeeCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector
) : CommandHandler {
  override val command = LIST_EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap {
        telegramClientConnector.getAllEmployeesByEmployerChatId(it)
      }
      .flatMapIterable { it }
      .map {
        telegramBot.execute(
          SendMessage(update.chatId,"Employee with username ${it.username}")
        )

      }.then(Mono.empty())
  }
}
