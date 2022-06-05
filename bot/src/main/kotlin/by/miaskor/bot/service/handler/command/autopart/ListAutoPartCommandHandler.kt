package by.miaskor.bot.service.handler.command.autopart

import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.domain.Command
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.handler.list.ListEntityHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ListAutoPartCommandHandler(
  private val listEntityHandler: ListEntityHandler,
  private val autoPartListCache: AbstractListCache
) : CommandHandler {
  override val command = Command.LIST_AUTO_PART

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .doOnNext { autoPartListCache.evict(it) }
      .flatMap { listEntityHandler.handle(update, CallbackCommand.AUTO_PARTS_PREV) }
  }
}
