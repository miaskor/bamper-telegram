package by.miaskor.bot.service.handler.list

import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.domain.ListEntityType.Companion.getByCallbackCommand
import by.miaskor.bot.service.cache.ListEntityCacheRegistry
import by.miaskor.bot.service.extension.chatId
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ListEntityHandler(
  private val listEntityCacheRegistry: ListEntityCacheRegistry,
  private val listEntityHandlerRegistry: ListEntityHandlerRegistry
) {

  fun handle(update: Update, callbackCommand: CallbackCommand): Mono<Unit> {
    val listEntityType = getByCallbackCommand(callbackCommand)
    return Mono.fromSupplier { listEntityCacheRegistry.lookup(listEntityType) }
      .doOnNext { it.populateIsNotExists(update) }
      .doOnNext { it.moveOffset(update.chatId, callbackCommand) }
      .flatMap { listEntityCache ->
        listEntityHandlerRegistry.lookup(listEntityType)
          .handle(update, listEntityCache)
      }
  }
}
