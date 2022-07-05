package by.miaskor.bot.service.handler.command.bamper

import by.miaskor.bot.domain.Command.LOG_OUT_BAMPER
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class LogOutBamperCommandHandler(
  private val telegramClientCache: TelegramClientCache,
  private val backCommandHandler: CommandHandler,
) : CommandHandler {
  override val command = LOG_OUT_BAMPER

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .doOnNext { telegramClientCache.updateBamperSessionId(it, "") }
      .flatMap { backCommandHandler.handle(update) }
  }
}
