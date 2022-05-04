package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class MainMenuHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : BotStateHandler {
  override val state: BotState = BotState.MAIN_MENU

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId())
      .flatMap(keyboardBuilder::build)
      .map {
        telegramBot.execute(
          SendMessage(update.chatId(), "123")
            .replyMarkup(it)
        )
      }.then(Mono.empty())
  }
}
