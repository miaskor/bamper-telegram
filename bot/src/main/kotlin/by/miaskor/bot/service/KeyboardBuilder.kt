package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.domain.BotState.CHANGING_LANGUAGE
import by.miaskor.bot.domain.BotState.CHOOSING_LANGUAGE
import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import reactor.core.publisher.Mono

class KeyboardBuilder(
  private val telegramClientCache: TelegramClientCache
) {

  fun build(chatId: Long): Mono<Keyboard> {
    return Mono.just(chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap(::build)
  }

  private fun build(telegramClient: TelegramClient): Mono<Keyboard> {
    return Mono.just(telegramClient.chatId)
      .resolveLanguage(KeyboardSettings::class)
      .map {
        when (telegramClient.currentBotState) {
          CHOOSING_LANGUAGE -> buildKeyboard(it.choosingLanguageMenu())
          MAIN_MENU -> buildKeyboard(it.mainMenu())
          CHANGING_LANGUAGE -> buildKeyboard(it.changingLanguageMenu())
          EMPLOYEES_MENU -> buildKeyboard(it.employeeMenu())
          ADDING_EMPLOYEE -> buildKeyboard(it.addingEmployee())
          else -> buildKeyboard(arrayOf(arrayOf("Something went wrong")))
        }
      }
  }

  private fun buildKeyboard(keyboardButtons: Array<Array<String>>): Keyboard {
    return ReplyKeyboardMarkup(keyboardButtons, true, true, true)
  }
}
