package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.GREETINGS
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class GreetingsHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : BotStateHandler {
  override val state: BotState = GREETINGS

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap { keyboardBuilder.build(it.message().chat().id()) }
      .map {
        telegramBot.execute(
          SendMessage(
            update.message().chat().id(),
            """Приветствую в боте bamper. Выбери язык.
              | Greetings for you in bamper bot. Choose language""".trimMargin()
          )
            .replyMarkup(it)
        )
      }.changeBotState { update.message().chat().id() }
  }
}
