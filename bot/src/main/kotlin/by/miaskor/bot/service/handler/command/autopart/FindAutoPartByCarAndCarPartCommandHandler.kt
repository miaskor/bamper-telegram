package by.miaskor.bot.service.handler.command.autopart

import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_CAR_AND_CAR_PART_ENTITY
import by.miaskor.bot.domain.ConstraintAutoPartListEntity
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.handler.list.ListEntityHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class FindAutoPartByCarAndCarPartCommandHandler(
  private val listEntityHandler: ListEntityHandler,
  private val autoPartListCache: AbstractListCache<ConstraintAutoPartListEntity>,
) : CommandHandler {
  override val command = FIND_AUTO_PART_BY_CAR_AND_CAR_PART_ENTITY

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .doOnNext { autoPartListCache.evict(it) }
      .flatMap { listEntityHandler.handle(update, CallbackCommand.FIND_AUTO_PARTS_PREV) }
  }
}
