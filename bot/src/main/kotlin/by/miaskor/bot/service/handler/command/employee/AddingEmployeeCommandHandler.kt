package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.api.domain.WorkerTelegramDto
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class AddingEmployeeCommandHandler(
  private val telegramClientConnector: TelegramClientConnector,
  private val workerTelegramConnector: WorkerTelegramConnector,
  private val telegramClientCache: TelegramClientCache
) {

  fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { getUsername(update) }
      .flatMap(telegramClientConnector::getByUsername)
      .switchIfEmpty(sendMessage(update.chatId, MessageSettings::failFoundEmployeeMessage))
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, telegramClientResponse: TelegramClientResponse): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .flatMap {
        if (telegramClientResponse.chatId == update.chatId) {
          sendMessage(update.chatId, MessageSettings::employeeIsYouMessage)
        } else {
          createEmployee(telegramClientResponse, update)
        }
      }
  }

  private fun createEmployee(telegramClientResponse: TelegramClientResponse, update: Update): Mono<Unit> {
    return Mono.just(telegramClientResponse.chatId)
      .map {
        WorkerTelegramDto(
          employerChatId = update.chatId,
          employeeChatId = it
        )
      }.flatMap { createEmployee(it, update, telegramClientResponse) }
  }

  private fun createEmployee(
    workerTelegramDto: WorkerTelegramDto,
    update: Update,
    telegramClientResponse: TelegramClientResponse
  ): Mono<Unit> {
    return Mono.fromSupplier {
      WorkerTelegramDto(
        employerChatId = workerTelegramDto.employeeChatId,
        employeeChatId = workerTelegramDto.employerChatId
      )
    }.flatMap(workerTelegramConnector::find)
      .switchIfEmpty(
        workerTelegramConnector.create(workerTelegramDto)
          .then(
            telegramClientCache.getTelegramClient(update.chatId)
              .map { it.addEmployee(Pair(telegramClientResponse.username, telegramClientResponse.chatId)) }
          )
          .then(sendMessage(update.chatId, MessageSettings::addingEmployeeSuccessMessage))
      )
      .flatMap { sendMessage(update.chatId, MessageSettings::employeeIsYourEmployerMessage, update.text) }
  }

  private fun getUsername(update: Update) = update.text.replace("@", "")
}
