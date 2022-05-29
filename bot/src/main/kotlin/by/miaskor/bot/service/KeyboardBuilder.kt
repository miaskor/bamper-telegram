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
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.BotState.REMOVING_EMPLOYEE
import by.miaskor.bot.domain.BotState.STORE_HOUSE_MENU
import by.miaskor.bot.domain.CallbackQuery
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.MODEL
import by.miaskor.bot.domain.CreatingCarStep.YEAR
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.extension.names
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

  @Suppress("UNCHECKED_CAST")
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

          STORE_HOUSE_MENU -> buildReplyKeyboard(it.storeHouseMenu())
          CREATING_AUTO_PART -> buildReplyKeyboard(it.creatingAutoPartMenu())
          CREATING_CAR -> buildReplyKeyboard(it.creatingCarMenu())
          DELETING_CAR -> buildReplyKeyboard(it.deletingCarMenu())
          DELETING_AUTO_PART -> buildReplyKeyboard(it.deletingAutoPartMenu())
          ADDING_EMPLOYEE_TO_STORE_HOUSE -> buildReplyKeyboard(it.addingEmployeeToStoreHouseMenu())
          else -> buildReplyKeyboard(listOf("Something went wrong"))
        }
      }
  }

  fun buildCreatingCarStepKeyboard(creatingCarStep: CreatingCarStep, keyboardSettings: KeyboardSettings): Keyboard {
    return when (creatingCarStep) {
      BRAND_NAME, MODEL, YEAR -> buildReplyKeyboard(keyboardSettings.keyboardForMandatorySteps())
      else -> buildReplyKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
    }
  }

  fun buildReplyKeyboard(keyboardButtons: List<String>): Keyboard {
    val buttons = keyboardButtons.chunked(2)
      .map { it.toTypedArray() }
      .toTypedArray()
    return ReplyKeyboardMarkup(buttons, true, false, true)
  }

  fun buildInlineKeyboard(keyboardButtons: List<String>, callbackQuery: List<CallbackQuery>): Keyboard {
    if (keyboardButtons.size != callbackQuery.size) {
      throw IllegalArgumentException("Keyboard buttons and callback queries sizes might be the same")
    }

    val buttons = keyboardButtons
      .zip(callbackQuery)
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
    val storeHouses = telegramClient.telegramClientStoreHouses
      .storeHouses
      .names()
      .toList()
    val storeHouseMenuKeyboards = keyboardSettings.choosingStoreHouseMenu()
    return storeHouses.plus(storeHouseMenuKeyboards)
  }
}
