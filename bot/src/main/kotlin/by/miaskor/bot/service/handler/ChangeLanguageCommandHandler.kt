package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.BotState.CHOOSE_LANGUAGE
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.domain.Language
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.CommandSettingsRegistry
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class ChangeLanguageCommandHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder,
  private val commandSettingsRegistry: CommandSettingsRegistry,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = CHANGE_LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update)
      .changeBotState(update::chatId, CHOOSE_LANGUAGE)
      .flatMap { keyboardBuilder.build(it.chatId) }
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .map { it.chatLanguage }
      .flatMap { Language.getByDomain(it) }
      .map { commandSettingsRegistry.lookup(it) }
      .map {
        telegramBot.execute(
          SendMessage(chatId, it.changeLanguageMessage())
            .replyMarkup(keyboard)
        )
      }.then(Mono.empty())
  }
}
