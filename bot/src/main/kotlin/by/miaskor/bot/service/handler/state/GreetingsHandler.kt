package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.CHOOSING_LANGUAGE
import by.miaskor.bot.domain.BotState.GREETINGS
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class GreetingsHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder,
  private val messageSettings: MessageSettings
) : BotStateHandler {
  override val state: BotState = GREETINGS

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, CHOOSING_LANGUAGE)
      .flatMap(keyboardBuilder::build)
      .map { keyboard ->
        telegramBot.sendMessageWithKeyboard(update.chatId, messageSettings.greetingsMessage().trimIndent(), keyboard)
      }
  }
}
