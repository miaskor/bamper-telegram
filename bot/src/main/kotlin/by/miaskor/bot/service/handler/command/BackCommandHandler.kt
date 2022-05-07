package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.Command.BACK
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class BackCommandHandler(
  private val telegramClientCache: TelegramClientCache,
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : CommandHandler {
  override val command = BACK

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { handle(it, update) }
  }

  private fun handle(telegramClient: TelegramClient, update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, telegramClient.previousBotState)
      .resolveLanguage(StateSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map {
        val sendMessage = when (telegramClient.currentBotState) {
          MAIN_MENU -> it.t1.mainMenuMessage()
          EMPLOYEES_MENU -> it.t1.employeesMenuMessage()
          else -> "Something bad happened"
        }
        telegramBot.execute(
          SendMessage(update.chatId, sendMessage)
            .replyMarkup(it.t2)
        )
      }.then(Mono.empty())
  }
}
