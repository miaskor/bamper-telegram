package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.api.domain.WorkerTelegramDto
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class RemovingEmployeeCommandHandler(
  private val telegramClientConnector: TelegramClientConnector,
  private val workerTelegramConnector: WorkerTelegramConnector,
  private val telegramClientCache: TelegramClientCache
) {

  fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { update.text.replace("@", "") }
      .flatMap { telegramClientConnector.getByUsername(it) }
      .switchIfEmpty(sendMessage(update.chatId, MessageSettings::failFoundEmployeeMessage, update.text))
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, telegramClientResponse: TelegramClientResponse): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .flatMap {
        if (telegramClientResponse.chatId == update.chatId) {
          sendMessage(update.chatId, MessageSettings::employeeIsYouMessage, update.text)
        } else {
          deleteEmployee(telegramClientResponse, update)
        }
      }
  }

  private fun deleteEmployee(telegramClientResponse: TelegramClientResponse, update: Update): Mono<Unit> {
    return Mono.just(telegramClientResponse.chatId)
      .map {
        WorkerTelegramDto(
          employerChatId = update.chatId,
          employeeChatId = it
        )
      }.flatMap(workerTelegramConnector::remove)
      .then(
        telegramClientCache.getTelegramClient(update.chatId)
          .map { it.removeEmployee(Pair(telegramClientResponse.username, telegramClientResponse.chatId)) }
      )
      .then(sendMessage(update.chatId, MessageSettings::removingEmployeeSuccessMessage, update.text))
  }
}
