package by.miaskor.bot.service

import by.miaskor.bot.domain.CallbackCommand.AUTO_PARTS_NEXT
import by.miaskor.bot.domain.CallbackCommand.AUTO_PARTS_PREV
import by.miaskor.bot.domain.CallbackCommand.CARS_NEXT
import by.miaskor.bot.domain.CallbackCommand.CARS_PREV
import by.miaskor.bot.service.handler.list.ListEntityHandler
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CallBackCommandHandler(
  private val listEntityHandler: ListEntityHandler
) {

  fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap {
        listEntityHandler.let {
          when (update.callbackQuery().data()) {
            CARS_PREV.name -> it.handle(update, CARS_PREV)
            CARS_NEXT.name -> it.handle(update, CARS_NEXT)
            AUTO_PARTS_PREV.name -> it.handle(update, AUTO_PARTS_PREV)
            AUTO_PARTS_NEXT.name -> it.handle(update, AUTO_PARTS_NEXT)
            else -> Mono.empty()
          }
        }
      }
  }
}
