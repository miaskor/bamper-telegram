package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.StateBot
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientRequest
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class ChooseLanguageHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache,
  private val telegramClientConnector: TelegramClientConnector,
) : StateHandler {
  override val state: StateBot = StateBot.CHOOSE_LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { buildKeyboard() }
      .map {
        val chatId = update.message().chat().id()
        telegramBot.execute(
          SendMessage(chatId, "Choose your language")
            .replyMarkup(it)
        )
      }.map {
        val message = update.message()
        TelegramClientRequest(
          chatId = message.chat().id().toString(),
          chatLanguage = mapLanguage(message.text()),
          username = message.chat().username(),
        )
      }
      .flatMap {
        telegramClientConnector.create(it) }
      .then(Mono.empty<Unit>())
      .doOnSuccess { telegramClientCache.populate(update.message().chat().id(), state.next()) }
  }

  private fun mapLanguage(lang: String): String {
    return when (lang) {
      "English" -> "EN"
      else -> "RU"
    }
  }

  private fun buildKeyboard(): Keyboard {
    return ReplyKeyboardMarkup(arrayOf("English", "Русский"))
      .oneTimeKeyboard(true)
      .resizeKeyboard(true)
      .selective(true)
  }
}
