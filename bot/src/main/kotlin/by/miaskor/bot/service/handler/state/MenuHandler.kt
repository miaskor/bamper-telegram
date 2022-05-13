package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class MenuHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder,
  override val state: BotState
) : BotStateHandler {
  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map {
        val message = when (state) {
          BotState.MAIN_MENU -> it.t1.mainMenuMessage()
          BotState.EMPLOYEES_MENU -> it.t1.employeesMenuMessage()
          BotState.STORE_HOUSE_MENU -> it.t1.storeHouseMenuMessage()
          else -> "Something bad happened in menuHandler"
        }
        telegramBot.sendMessageWithKeyboard(update.chatId, message, it.t2)
      }
  }
}
