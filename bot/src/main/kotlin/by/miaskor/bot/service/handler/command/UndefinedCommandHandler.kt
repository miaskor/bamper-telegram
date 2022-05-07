package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.Command.UNDEFINED
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class UndefinedCommandHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : CommandHandler {
  override val command = UNDEFINED

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .changeBotState(update::chatId, MAIN_MENU)
      .flatMap { keyboardBuilder.build(it.chatId) }
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(StateSettings::class)
      .map {
        telegramBot.execute(
          SendMessage(chatId, it.mainMenuMessage())
            .replyMarkup(keyboard)
        )
      }.then(Mono.empty())
  }
}
