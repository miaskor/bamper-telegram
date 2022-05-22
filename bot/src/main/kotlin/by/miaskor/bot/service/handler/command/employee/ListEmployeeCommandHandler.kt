package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.LIST_EMPLOYEE
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ListEmployeeCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = LIST_EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .flatMap { handle(it, update) }
  }

  private fun handle(messageSettings: MessageSettings, update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { handle(it, update, messageSettings) }
  }

  private fun handle(telegramClient: TelegramClient, update: Update, messageSettings: MessageSettings): Mono<Unit> {
    return Mono.just(telegramClient)
      .filter { it.employeeUsernames.isModified }
      .switchIfEmpty(
        Flux.fromIterable(telegramClient.employeeUsernames.employees)
          .map { TelegramClientResponse(username = it) }
          .collectList()
          .flatMap { sendEmployees(it, messageSettings, telegramClient, update) }
          .doOnSuccess { log.info("Cache of employees was used") }
          .then(Mono.empty())
      )
      .flatMap { telegramClientConnector.getAllEmployeesByEmployerChatId(update.chatId) }
      .flatMap { sendEmployees(it, messageSettings, telegramClient, update) }
  }

  private fun sendEmployees(
    employees: List<TelegramClientResponse>,
    messageSettings: MessageSettings,
    telegramClient: TelegramClient,
    update: Update
  ): Mono<Unit> {
    return Flux.fromIterable(employees)
      .map { telegramBot.sendMessage(update.chatId, messageSettings.employeeMessage().format(it.username)) }
      .then(
        Mono.fromSupplier { employees.map { it.username } }
          .map { telegramClient.refreshEmployees(it) }
      )
  }

  companion object {
    private val log = LogManager.getLogger()
  }
}
