package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.KeyboardBuilder
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class MainMenuHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : BotStateHandler {
  override val state: BotState = BotState.MAIN_MENU

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { update }
      .flatMap { keyboardBuilder.build(it.message().chat().id()) }
      .map {

        val replyKeyboardRemove = ReplyKeyboardRemove()
        val chatId = update.message().chat().id()
        telegramBot.execute(
          SendMessage(chatId, "123")
            .replyMarkup(replyKeyboardRemove)
        )
        telegramBot.execute(
          SendMessage(chatId, "123")
            .replyMarkup(it)
        )
      }.then(Mono.empty())
  }
}
