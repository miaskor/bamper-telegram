package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.CHOOSING_LANGUAGE
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class ChoosingLanguageHandler(
  private val telegramBot: TelegramBot,
  private val stateSettings: StateSettings
) : BotStateHandler {
  override val state: BotState = CHOOSING_LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier {
      telegramBot.execute(
        SendMessage(update.chatId, stateSettings.chooseLanguageFailMessage())
      )
    }.then(Mono.empty())
  }
}
