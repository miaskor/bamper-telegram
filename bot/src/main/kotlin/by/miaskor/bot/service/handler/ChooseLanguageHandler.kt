package by.miaskor.bot.service.handler

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.CHOOSE_LANGUAGE
import by.miaskor.bot.domain.Language.Companion.getByFullLanguage
import by.miaskor.bot.domain.Language.Companion.isLanguageExists
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.text
import by.miaskor.bot.service.username
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientRequest
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class ChooseLanguageHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector,
  private val telegramClientCache: TelegramClientCache,
  private val stateSettings: StateSettings,
  private val keyboardBuilder: KeyboardBuilder
) : BotStateHandler {
  override val state: BotState = CHOOSE_LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.text())
      .filter(::isLanguageExists)
      .switchIfEmpty(
        Mono.fromSupplier {
          telegramBot.execute(
            SendMessage(update.chatId(), stateSettings.chooseLanguageFailMessage())
          )
        }.then(Mono.empty())
      )
      .flatMap { processMessage(update) }
  }

  private fun processMessage(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap(::createTelegramClient)
      .then(Mono.defer { sendMessage(update) })
  }

  private fun sendMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId())
      .flatMap(keyboardBuilder::build)
      .map {
        telegramBot.execute(
          SendMessage(update.chatId(), stateSettings.mainMenuMessage())
            .replyMarkup(it)
        )
      }
      .changeBotState { update.chatId() }
  }

  private fun createTelegramClient(update: Update): Mono<Unit> {
    return Mono.just(update.text())
      .flatMap { getByFullLanguage(it) }
      .map { language ->
        telegramClientCache.updateChatLanguage(update.chatId(), language.domain)
        language
      }
      .map {
        TelegramClientRequest(
          chatId = update.chatId().toString(),
          chatLanguage = it.domain,
          username = update.username(),
        )
      }
      .flatMap(telegramClientConnector::create)
  }
}
