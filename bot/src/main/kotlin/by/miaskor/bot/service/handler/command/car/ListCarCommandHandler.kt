package by.miaskor.bot.service.handler.command.car

import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.domain.Command.LIST_CAR
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.handler.list.ListEntityHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ListCarCommandHandler(
  private val listEntityHandler: ListEntityHandler,
  private val autoPartListCache: AbstractListCache<ListEntity>
) : CommandHandler {
  override val command = LIST_CAR

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .doOnNext { autoPartListCache.evict(it) }
      .flatMap { listEntityHandler.handle(update, CallbackCommand.CARS_PREV) }
  }
}
