package by.miaskor.bot.service.handler.command.language

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.Command.LANGUAGE
import by.miaskor.bot.domain.Language
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.MessageSender.sendMessageWithKeyboard
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import by.miaskor.bot.service.username
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientRequest
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class LanguageCommandHandler(
  private val telegramClientConnector: TelegramClientConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap(::upsertTelegramClient)
      .then(Mono.defer { sendMessage(update) })
  }

  private fun sendMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, MAIN_MENU)
      .flatMap { sendMessageWithKeyboard(update.chatId, MessageSettings::mainMenuMessage) }
  }

  private fun upsertTelegramClient(update: Update): Mono<Unit> {
    return Mono.just(update.text)
      .flatMap { Language.getByFullLanguage(it) }
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
