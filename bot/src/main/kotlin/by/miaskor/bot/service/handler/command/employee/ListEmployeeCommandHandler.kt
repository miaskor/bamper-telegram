package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.CommandSettings
import by.miaskor.bot.domain.Command.LIST_EMPLOYEE
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.TelegramClientConnector
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ListEmployeeCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector
) : CommandHandler {
  override val command = LIST_EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(CommandSettings::class)
      .flatMap { handle(it, update) }
  }

  private fun handle(commandSettings: CommandSettings, update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap {
        telegramClientConnector.getAllEmployeesByEmployerChatId(it)
      }
      .flatMapIterable { it }
      .map { telegramBot.sendMessage(update.chatId, commandSettings.employeeUsernameMessage().format(it.username)) }
      .then(Mono.empty())
  }
}
