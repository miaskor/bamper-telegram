package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class MenuHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder,
  override val state: BotState
) : BotStateHandler {
  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map { (messageSettings, keyboard) ->
        val message = when (state) {
          BotState.MAIN_MENU -> messageSettings.mainMenuMessage()
          BotState.EMPLOYEES_MENU -> messageSettings.employeesMenuMessage()
          BotState.MODIFICATION_STORE_HOUSE_MENU -> messageSettings.storeHouseMenuMessage()
          else -> "Something bad happened in menuHandler"
        }
        telegramBot.sendMessageWithKeyboard(update.chatId, message, keyboard)
      }
  }
}
