package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.*
import by.miaskor.bot.domain.Command
import by.miaskor.bot.domain.Command.ADD_EMPLOYEE
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class AddEmployeeCommandHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
)  : CommandHandler {
  override val command = ADD_EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, ADDING_EMPLOYEE)
      .flatMap(keyboardBuilder::build)
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(StateSettings::class)
      .map {
        telegramBot.execute(
          SendMessage(chatId, it.addingEmployeeMessage())
            .replyMarkup(keyboard)
        )
      }
  }
}
