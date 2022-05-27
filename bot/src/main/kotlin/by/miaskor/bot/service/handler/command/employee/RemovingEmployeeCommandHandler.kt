package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.REMOVING_EMPLOYEE
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.api.domain.WorkerTelegramDto
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class RemovingEmployeeCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector,
  private val workerTelegramConnector: WorkerTelegramConnector,
  private val keyboardBuilder: KeyboardBuilder,
  private val telegramClientCache: TelegramClientCache
) : AbstractEmployeeCommandHandler(telegramBot) {

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { update.text.replace("@", "") }
      .flatMap { telegramClientConnector.getByUsername(it) }
      .switchIfEmpty(sendFailMessage(update))
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, telegramClientResponse: TelegramClientResponse): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .flatMap {
        if (telegramClientResponse.chatId == update.chatId) {
          sendIsYouMessage(update)
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
          .map { it.modifiedEmployees() }
      )
      .then(sendSuccessRemovingMessage(update))
  }

  private fun sendSuccessRemovingMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map {
        telegramBot.sendMessageWithKeyboard(update.chatId, it.t1.removingEmployeeSuccessMessage(), it.t2)
      }.then(Mono.empty())
  }

  override fun commandInState() = REMOVING_EMPLOYEE
}
