package by.miaskor.bot.service.handler.command.car

import by.miaskor.bot.domain.CallbackQuery
import by.miaskor.bot.domain.Command.LIST_CAR
import by.miaskor.bot.service.ListEntityCache
import by.miaskor.bot.service.ListEntityHandler
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ListCarCommandHandler(
  private val listEntityHandler: ListEntityHandler,
  private val listEntityCache: ListEntityCache
) : CommandHandler {
  override val command = LIST_CAR

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .doOnNext { listEntityCache.evict(it) }
      .flatMap { listEntityHandler.handle(update, CallbackQuery.CARS_PREV) }
  }
}
