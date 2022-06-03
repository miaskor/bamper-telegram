package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.EMPLOYEE_TO_STORE_HOUSE
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerStoreHouseConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.api.domain.WorkerTelegramDto
import by.miaskor.domain.api.domain.WorkerTelegramStoreHouseDto
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class AddingEmployeeToStoreHouseCommandHandler(
  private val telegramClientConnector: TelegramClientConnector,
  private val telegramClientCache: TelegramClientCache,
  private val workerStoreHouseConnector: WorkerStoreHouseConnector,
  private val workerTelegramConnector: WorkerTelegramConnector,
) : CommandHandler {
  override val command = EMPLOYEE_TO_STORE_HOUSE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { getUsername(update) }
      .flatMap(telegramClientConnector::getByUsername)
      .switchIfEmpty(sendMessage(update.chatId, MessageSettings::failFoundEmployeeMessage))
      .flatMap { telegramClientResponse -> handle(update, telegramClientResponse) }
  }

  private fun handle(update: Update, telegramClientResponse: TelegramClientResponse): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .flatMap {
        if (telegramClientResponse.chatId == update.chatId) {
          sendMessage(update.chatId, MessageSettings::employeeIsYouMessage)
        } else {
          addEmployeeToStoreHouse(telegramClientResponse, update, it)
        }
      }
  }

  private fun addEmployeeToStoreHouse(
    telegramClientResponse: TelegramClientResponse,
    update: Update,
    telegramClient: TelegramClient
  ): Mono<Unit> {
    return Mono.fromSupplier {
      WorkerTelegramDto(
        employeeChatId = telegramClientResponse.chatId,
        employerChatId = update.chatId
      )
    }.flatMap(workerTelegramConnector::find)
      .switchIfEmpty(sendMessage(update.chatId, MessageSettings::notYourEmployeeMessage))
      .map {
        WorkerTelegramStoreHouseDto(
          employeeTelegramId = it.employeeChatId,
          privilege = getPrivilege(update),
          storeHouseId = telegramClient.currentStoreHouseId()
        )
      }
      .flatMap {
        workerStoreHouseConnector.create(it)
          .then(sendMessage(update.chatId, MessageSettings::addingEmployeeSuccessMessage))
      }
  }

  private fun getPrivilege(update: Update): String {
    val privilege = update.text.substringAfter(", ")
    return if (
      privilege.equals("modification", true)
      || privilege.equals("модификация", true)
    ) {
      "M"
    } else {
      "R"
    }
  }

  private fun getUsername(update: Update) = update.text
    .replace("@", "")
    .substringBefore(", ")
}
