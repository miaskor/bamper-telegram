package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.Command.UNDEFINED
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class UndefinedCommandHandler(
  private val telegramBot: TelegramBot
) : CommandHandler {
  override val command = UNDEFINED

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(StateSettings::class)
      .map {
        telegramBot.execute(
          SendMessage(update.chatId, it.undefinedCommandMessage())
        )
      }.then(Mono.empty())
  }
}
