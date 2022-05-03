package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
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
    return Mono.just(telegramClient)
      .flatMap { LanguageResolver.resolve(it.chatLanguage) }
      .map(keyboardSettingsRegistry::lookup)
      .map {
        when (telegramClient.botState.next()) {
          BotState.CHOOSE_LANGUAGE -> buildKeyboard(arrayOf(arrayOf("English", "Русский")))
          BotState.MAIN_MENU -> buildKeyboard(
            arrayOf(
              it.mainMenuFirstRow(),
              it.mainMenuSecondRow(),
              it.mainMenuThirdRow()
            )
          )
          else -> buildKeyboard(arrayOf(arrayOf("Something went wrong")))
        }
      }
  }

  private fun buildKeyboard(keyboardButtons: Array<Array<String>>): Keyboard {
    return ReplyKeyboardMarkup(keyboardButtons, true, true, true)
  }
}
