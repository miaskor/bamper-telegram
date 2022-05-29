package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.LIST_EMPLOYEE
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ListEmployeeCommandHandler(
  private val telegramClientConnector: TelegramClientConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = LIST_EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { handle(it, update) }
  }

  private fun handle(telegramClient: TelegramClient, update: Update): Mono<Unit> {
    return Mono.just(telegramClient)
      .filter { it.isEmployeesModified() }
      .switchIfEmpty(
        Flux.fromIterable(telegramClient.getEmployees())
          .map { TelegramClientResponse(username = it.first) }
          .doOnNext { telegramClient.addEmployee(Pair(it.username, it.chatId)) }
          .flatMap { sendMessage<TelegramClientResponse>(update.chatId, MessageSettings::employeeMessage, it.username) }
          .doOnComplete { log.info("Cache of employees was used") }
          .then(Mono.empty())
      )
      .flatMap { telegramClientConnector.getAllEmployeesByEmployerChatId(update.chatId) }
      .flatMapIterable { it }
      .doOnNext { telegramClient.addEmployee(Pair(it.username, it.chatId)) }
      .flatMap { sendMessage<TelegramClientResponse>(update.chatId, MessageSettings::employeeMessage, it.username) }
      .then(Mono.empty())
  }

  companion object {
    private val log = LogManager.getLogger()
  }
}
