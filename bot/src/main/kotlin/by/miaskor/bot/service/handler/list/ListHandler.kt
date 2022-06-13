package by.miaskor.bot.service.handler.list

import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType
import by.miaskor.bot.service.cache.AbstractListCache
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

interface ListHandler<T : ListEntity> {
  val listEntityType: ListEntityType
  fun handle(update: Update, listEntityCache: AbstractListCache<T>): Mono<Unit>
}