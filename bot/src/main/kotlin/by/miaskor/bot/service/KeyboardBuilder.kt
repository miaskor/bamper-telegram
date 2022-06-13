package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE_TO_STORE_HOUSE
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
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART_BY_PART_NUMBER
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
          CHOOSING_LANGUAGE -> buildReplyKeyboard(it.choosingLanguageMenu())
          MAIN_MENU -> buildReplyKeyboard(it.mainMenu())
          CHANGING_LANGUAGE -> buildReplyKeyboard(it.changingLanguageMenu())
          EMPLOYEES_MENU -> buildReplyKeyboard(it.employeeMenu())
          ADDING_EMPLOYEE -> buildReplyKeyboard(it.addingEmployee())
          REMOVING_EMPLOYEE -> buildReplyKeyboard(it.removingEmployee())
          CREATING_STORE_HOUSE -> buildReplyKeyboard(it.creatingStoreHouseMenu())
          CHOOSING_STORE_HOUSE -> {
            val buttons = addStoreHousesToKeyboard(telegramClient, it)
            buildReplyKeyboard(buttons)
          }

          MODIFICATION_STORE_HOUSE_MENU -> buildReplyKeyboard(it.modificationStoreHouseMenu())
          READ_ONLY_STORE_HOUSE_MENU -> buildReplyKeyboard(it.readStoreHouseMenu())
          CREATING_AUTO_PART -> buildReplyKeyboard(it.creatingAutoPartMenu())
          CREATING_CAR -> buildReplyKeyboard(it.creatingCarMenu())
          DELETING_CAR -> buildReplyKeyboard(it.deletingCarMenu())
          DELETING_AUTO_PART -> buildReplyKeyboard(it.deletingAutoPartMenu())
          ADDING_EMPLOYEE_TO_STORE_HOUSE -> buildReplyKeyboard(it.addingEmployeeToStoreHouseMenu())
          FINDING_AUTO_PART -> buildReplyKeyboard(it.findAutoPartMenu())
          FINDING_AUTO_PART_BY_PART_NUMBER -> buildReplyKeyboard(it.defaultMenu())
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
    keyboardSettings: KeyboardSettings
  ): List<String> {
    val storeHouses = telegramClient.storeHouses().map { it.name }
    val storeHouseMenuKeyboards = keyboardSettings.choosingStoreHouseMenu()
    return storeHouses.plus(storeHouseMenuKeyboards)
  }
}
