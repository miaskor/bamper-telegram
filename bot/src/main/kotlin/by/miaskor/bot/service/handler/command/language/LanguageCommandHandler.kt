package by.miaskor.bot.service.handler.command.language

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command.LANGUAGE
import by.miaskor.bot.domain.Language
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import by.miaskor.bot.service.username
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientRequest
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class LanguageCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector,
  private val telegramClientCache: TelegramClientCache,
  private val keyboardBuilder: KeyboardBuilder
) : CommandHandler {
  override val command = LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .flatMap(::upsertTelegramClient)
      .then(Mono.defer { sendMessage(update) })
  }

  private fun sendMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, BotState.MAIN_MENU)
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
