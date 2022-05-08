package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.CHOOSE_LANGUAGE
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.Language.Companion.getByFullLanguage
import by.miaskor.bot.domain.Language.Companion.isLanguageExists
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.text
import by.miaskor.bot.service.username
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientRequest
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
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

  //TODO(maybe in CommandResolver create method that will be executed when handler couldn't manage message
  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.text)
      .filter(::isLanguageExists)
      .switchIfEmpty(
        Mono.fromSupplier {
          telegramBot.execute(
            SendMessage(update.chatId, stateSettings.chooseLanguageFailMessage())
          )
        }.then(Mono.empty())
      )
      .flatMap { processMessage(update) }
  }

  private fun processMessage(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap(::upsertTelegramClient)
      .then(Mono.defer { sendMessage(update) })
  }

  private fun sendMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, MAIN_MENU)
      .flatMap(keyboardBuilder::build)
      .flatMap { sendMessage(it, update.chatId) }
  }

  private fun sendMessage(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(StateSettings::class)
      .map {
        telegramBot.execute(
          SendMessage(chatId, it.mainMenuMessage())
            .replyMarkup(keyboard)
        )
      }
  }

  private fun upsertTelegramClient(update: Update): Mono<Unit> {
    return Mono.just(update.text)
      .flatMap { getByFullLanguage(it) }
      .map { language ->
        telegramClientCache.updateChatLanguage(update.chatId, language.domain)
        language
      }
      .map {
        TelegramClientRequest(
          chatId = update.chatId,
          chatLanguage = it.domain,
          username = update.username,
        )
      }
      .flatMap(telegramClientConnector::upsert)
  }
}
