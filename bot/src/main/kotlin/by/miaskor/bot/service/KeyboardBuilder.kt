package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState.CHOOSE_LANGUAGE
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.Language
import by.miaskor.bot.domain.TelegramClient
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import reactor.core.publisher.Mono

class KeyboardBuilder(
  private val keyboardSettingsRegistry: KeyboardSettingsRegistry,
  private val telegramClientCache: TelegramClientCache
) {

  fun build(chatId: Long): Mono<Keyboard> {
    return Mono.just(chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap(::build)
  }

  private fun build(telegramClient: TelegramClient): Mono<Keyboard> {
    return Mono.just(telegramClient.chatLanguage)
      .flatMap { Language.getByDomain(it) }
      .map(keyboardSettingsRegistry::lookup)
      .map {
        when (telegramClient.botState) {
          CHOOSE_LANGUAGE -> buildKeyboard(it.chooseLanguageMenu())
          MAIN_MENU -> buildKeyboard(it.mainMenu())
          else -> buildKeyboard(arrayOf(arrayOf("Something went wrong")))
        }
      }
  }

  private fun buildKeyboard(keyboardButtons: Array<Array<String>>): Keyboard {
    return ReplyKeyboardMarkup(keyboardButtons, true, true, true)
  }
}
