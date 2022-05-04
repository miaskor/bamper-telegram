package by.miaskor.bot.service.handler

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.GREETINGS
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class GreetingsHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder,
  private val stateSettings: StateSettings
) : BotStateHandler {
  override val state: BotState = GREETINGS

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(keyboardBuilder::build)
      .map { keyboard ->
        telegramBot.execute(
          SendMessage(update.chatId, stateSettings.greetingsMessage().trimIndent())
            .replyMarkup(keyboard)
        )
      }.changeBotState(update::chatId)
  }
}
