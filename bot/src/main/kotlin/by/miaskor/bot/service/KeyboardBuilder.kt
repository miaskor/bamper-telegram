package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE_TO_STORE_HOUSE
import by.miaskor.bot.domain.BotState.AUTHORIZATION_BAMPER
import by.miaskor.bot.domain.BotState.BAMPER_MENU
import by.miaskor.bot.domain.BotState.CHANGING_LANGUAGE
import by.miaskor.bot.domain.BotState.CHOOSING_LANGUAGE
import by.miaskor.bot.domain.BotState.CHOOSING_STORE_HOUSE
import by.miaskor.bot.domain.BotState.CREATING_AUTO_PART
import by.miaskor.bot.domain.BotState.CREATING_CAR
import by.miaskor.bot.domain.BotState.CREATING_STORE_HOUSE
import by.miaskor.bot.domain.BotState.DELETING_AUTO_PART
import by.miaskor.bot.domain.BotState.DELETING_CAR
import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART_BY_CAR_AND_CAR_PART
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART_BY_ID
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART_BY_PART_NUMBER
import by.miaskor.bot.domain.BotState.IMPORTING_AUTO_PART_BY_ID
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.BotState.MODIFICATION_STORE_HOUSE_MENU
import by.miaskor.bot.domain.BotState.READ_ONLY_STORE_HOUSE_MENU
import by.miaskor.bot.domain.BotState.REMOVING_EMPLOYEE
import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.cache.TelegramClientCache
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import reactor.core.publisher.Mono

class KeyboardBuilder(
  private val telegramClientCache: TelegramClientCache,
) {

  fun build(chatId: Long): Mono<Keyboard> {
    return Mono.just(chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap(::build)
  }

  private fun build(telegramClient: TelegramClient): Mono<Keyboard> {
    return Mono.just(telegramClient.chatId)
      .resolveLanguage(KeyboardSettings::class)
      .map { keyboardSettings ->
        when (telegramClient.currentBotState) {
          CHOOSING_LANGUAGE -> buildReplyKeyboard(keyboardSettings.choosingLanguageMenu())
          MAIN_MENU -> buildReplyKeyboard(keyboardSettings.mainMenu())
          CHANGING_LANGUAGE -> buildReplyKeyboard(keyboardSettings.changingLanguageMenu())
          EMPLOYEES_MENU -> buildReplyKeyboard(keyboardSettings.employeeMenu())
          CHOOSING_STORE_HOUSE -> {
            val buttons = addStoreHousesToKeyboard(telegramClient, keyboardSettings)
            buildReplyKeyboard(buttons)
          }

          MODIFICATION_STORE_HOUSE_MENU -> buildReplyKeyboard(keyboardSettings.modificationStoreHouseMenu())
          READ_ONLY_STORE_HOUSE_MENU -> buildReplyKeyboard(keyboardSettings.readStoreHouseMenu())
          FINDING_AUTO_PART -> buildReplyKeyboard(keyboardSettings.findAutoPartMenu())
          BAMPER_MENU -> buildReplyKeyboard(keyboardSettings.bamperMenu())
          FINDING_AUTO_PART_BY_PART_NUMBER,
          FINDING_AUTO_PART_BY_CAR_AND_CAR_PART,
          FINDING_AUTO_PART_BY_ID,
          AUTHORIZATION_BAMPER,
          REMOVING_EMPLOYEE,
          ADDING_EMPLOYEE,
          CREATING_CAR,
          DELETING_CAR,
          DELETING_AUTO_PART,
          CREATING_AUTO_PART,
          CREATING_STORE_HOUSE,
          ADDING_EMPLOYEE_TO_STORE_HOUSE,
          IMPORTING_AUTO_PART_BY_ID,
          -> buildReplyKeyboard(keyboardSettings.defaultMenu())

          else -> buildReplyKeyboard(listOf("Something went wrong"))
        }
      }
  }

  fun buildReplyKeyboard(keyboardButtons: List<String>): Keyboard {
    val buttons = keyboardButtons.chunked(2)
      .map { it.toTypedArray() }
      .toTypedArray()
    return ReplyKeyboardMarkup(buttons, true, false, true)
  }

  fun buildInlineKeyboard(keyboardButtons: List<String>, callbackCommand: List<CallbackCommand>): Keyboard {
    if (keyboardButtons.size != callbackCommand.size) {
      throw IllegalArgumentException("Keyboard buttons and callback queries sizes might be the same")
    }

    val buttons = keyboardButtons
      .zip(callbackCommand)
      .map { InlineKeyboardButton(it.first).callbackData(it.second.name) }
      .chunked(2)
      .map { it.toTypedArray() }
      .toTypedArray()
    return InlineKeyboardMarkup(*buttons)
  }

  private fun addStoreHousesToKeyboard(
    telegramClient: TelegramClient,
    keyboardSettings: KeyboardSettings,
  ): List<String> {
    val storeHouses = telegramClient.storeHouses().map { it.name }
    val storeHouseMenuKeyboards = keyboardSettings.defaultMenu()
    return storeHouses.plus(storeHouseMenuKeyboards)
  }
}
