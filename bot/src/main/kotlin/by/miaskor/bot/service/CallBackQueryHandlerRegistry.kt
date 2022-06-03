package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_NEXT
import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_PREV
import by.miaskor.bot.domain.CallbackQuery.CARS_NEXT
import by.miaskor.bot.domain.CallbackQuery.CARS_PREV
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CallBackQueryHandlerRegistry(
  private val listEntityHandler: ListEntityHandler
) {

  fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(KeyboardSettings::class)
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
