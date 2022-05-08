package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.service.CommandResolver
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.api.domain.WorkerTelegramRequest
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class AddingEmployeeHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector,
  private val workerTelegramConnector: WorkerTelegramConnector,
  private val keyboardBuilder: KeyboardBuilder
) : BotStateHandler {
  override val state = ADDING_EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.text)
      .filter { it.isNotBlank() }
      .flatMap { telegramClientConnector.getByUsername(it) }
      .switchIfEmpty(sendFailMessage(update))
      .flatMap { createEmployee(it, update) }
  }

  private fun createEmployee(telegramClientResponse: TelegramClientResponse, update: Update): Mono<Unit> {
    return Mono.just(telegramClientResponse.chatId)
      .map {
        WorkerTelegramRequest(
          employerChatId = update.chatId,
          employeeChatId = it
        )
      }.flatMap(workerTelegramConnector::create)
      .then(sendMessage(update))
  }

  private fun sendMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(StateSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map {
        telegramBot.execute(
          SendMessage(update.chatId, it.t1.addingEmployeeSuccessMessage())
            .replyMarkup(it.t2)
        )
      }.then(Mono.empty())
  }

  private fun sendFailMessage(update: Update): Mono<TelegramClientResponse> {
    return Mono.just(update.chatId)
      .resolveLanguage(StateSettings::class)
      .map {
        telegramBot.execute(
          SendMessage(update.chatId, it.addingEmployeeFailMessage().format(update.text))
        )
      }.then(Mono.empty())
  }
}
