package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.StateBot
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class MainMenuHandler(
  private val telegramBot: TelegramBot
) : StateHandler {
  override val state: StateBot = StateBot.MAIN_MENU

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { update }
      .map {
        val replyKeyboardMarkup = ReplyKeyboardMarkup(
          arrayOf("Работники", "Создать склад"),
          arrayOf("Работники", "Создать склад"),
          arrayOf("Работники", "Создать склад")
        )
          .oneTimeKeyboard(true)
          .resizeKeyboard(true)
          .selective(true)
        val chatId = it.message().chat().id()
        telegramBot.execute(
          SendMessage(chatId, "")
            .replyMarkup(replyKeyboardMarkup)
        )

      }.then(Mono.empty())
  }
}
