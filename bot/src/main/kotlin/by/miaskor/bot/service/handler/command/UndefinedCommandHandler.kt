package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.CommandSettings
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

class UndefinedCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = UNDEFINED

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(CommandSettings::class)
      .zipWith(telegramClientCache.getTelegramClient(update.chatId))
      .map {
        val message = when (it.t2.currentBotState) {
          CHOOSING_LANGUAGE, CHANGING_LANGUAGE -> it.t1.chooseLanguageFailMessage()
          ADDING_EMPLOYEE -> it.t1.incorrectUsernameFormatMessage()
          REMOVING_EMPLOYEE -> it.t1.incorrectUsernameFormatMessage()
          CREATING_STORE_HOUSE -> it.t1.incorrectStoreHouseNameFormatMessage()
          else -> it.t1.undefinedCommandMessage()
        }
        telegramBot.sendMessage(update.chatId, message)
      }.then(Mono.empty())
  }
}
