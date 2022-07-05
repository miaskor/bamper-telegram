package by.miaskor.bot.service.handler.command.bamper

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.AUTHORIZATION_BAMPER
import by.miaskor.bot.domain.BotState.BAMPER_MENU
import by.miaskor.bot.domain.Command.LOG_IN_BAMPER
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.MessageSender.sendMessageWithKeyboard
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class LogInBamperCommandHandler(
  private val telegramClientCache: TelegramClientCache,
) : CommandHandler {
  override val command = LOG_IN_BAMPER

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .filter { telegramClient -> telegramClient.isAuth() }
      .switchIfEmpty(
        Mono.just(update)
          .changeBotState(update::chatId, AUTHORIZATION_BAMPER)
          .flatMap { sendMessageWithKeyboard(update.chatId, MessageSettings::authMessage) }
      )
      .changeBotState(update::chatId, BAMPER_MENU)
      .flatMap { sendMessageWithKeyboard(update.chatId, MessageSettings::bamperMenuMessage) }
  }
}
