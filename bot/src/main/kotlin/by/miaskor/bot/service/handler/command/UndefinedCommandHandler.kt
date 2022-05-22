package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.domain.BotState.CHANGING_LANGUAGE
import by.miaskor.bot.domain.BotState.CHOOSING_LANGUAGE
import by.miaskor.bot.domain.BotState.CREATING_STORE_HOUSE
import by.miaskor.bot.domain.BotState.REMOVING_EMPLOYEE
import by.miaskor.bot.domain.Command.UNDEFINED
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class UndefinedCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = UNDEFINED

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(telegramClientCache.getTelegramClient(update.chatId))
      .map { (messageSettings, telegramClient) ->
        val message = when (telegramClient.currentBotState) {
          CHOOSING_LANGUAGE, CHANGING_LANGUAGE -> messageSettings.chooseLanguageFailMessage()
          ADDING_EMPLOYEE -> messageSettings.incorrectUsernameFormatMessage()
          REMOVING_EMPLOYEE -> messageSettings.incorrectUsernameFormatMessage()
          CREATING_STORE_HOUSE -> messageSettings.incorrectStoreHouseNameFormatMessage()
          else -> messageSettings.undefinedCommandMessage()
        }
        telegramBot.sendMessage(update.chatId, message)
      }.then(Mono.empty())
  }
}
