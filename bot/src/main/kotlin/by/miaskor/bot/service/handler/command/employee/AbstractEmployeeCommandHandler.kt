package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.text
import by.miaskor.domain.api.domain.TelegramClientResponse
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

abstract class AbstractEmployeeCommandHandler(
  private val telegramBot: TelegramBot
) {

  fun sendFailMessage(update: Update): Mono<TelegramClientResponse> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(update.chatId, it.failFoundEmployeeMessage().format(update.text))
      }.then(Mono.empty())
  }

  fun sendIsYouMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(update.chatId, it.employeeIsYouMessage().format(update.text))
      }.then(Mono.empty())
  }

  abstract fun handle(update: Update): Mono<Unit>
  abstract fun commandInState(): BotState
}
