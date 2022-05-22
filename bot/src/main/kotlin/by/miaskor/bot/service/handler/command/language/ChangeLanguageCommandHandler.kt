package by.miaskor.bot.service.handler.command.language

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.CHANGING_LANGUAGE
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.handler.command.CommandHandler
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import reactor.core.publisher.Mono

class ChangeLanguageCommandHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : CommandHandler {
  override val command = CHANGE_LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, CHANGING_LANGUAGE)
      .flatMap(keyboardBuilder::build)
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessageWithKeyboard(chatId, it.changeLanguageMessage(), keyboard)
      }
  }
}
