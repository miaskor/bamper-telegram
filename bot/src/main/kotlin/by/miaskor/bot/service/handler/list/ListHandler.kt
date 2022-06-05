package by.miaskor.bot.service.handler.list

import by.miaskor.bot.domain.ListEntityType
import by.miaskor.bot.service.cache.AbstractListCache
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

interface ListHandler {
  val listEntityType: ListEntityType
  fun handle(update: Update, listEntityCache: AbstractListCache): Mono<Unit>
}