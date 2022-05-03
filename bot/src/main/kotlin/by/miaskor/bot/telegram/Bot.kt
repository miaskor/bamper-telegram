package by.miaskor.bot.telegram

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.handler.BotStateHandlerRegistry
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class Bot(
  private val telegramBot: TelegramBot,
  private val botStateHandlerRegistry: BotStateHandlerRegistry,
  private val telegramClientCache: TelegramClientCache,
) {

  init {
    telegramBot.setUpdatesListener { updates ->
      Flux.fromIterable(updates)
        .doOnNext { log.info("Processing update=$it") }
        .flatMap(::processChatId)
        .then(Mono.just(CONFIRMED_UPDATES_ALL))
        .toFuture()
        .get()
    }
  }

  private fun processChatId(update: Update): Mono<Unit> {
    return Mono.fromSupplier { update.message().chat().id() }
      .flatMap(telegramClientCache::getTelegramClient)
      .map { it.botState }
      .flatMap(botStateHandlerRegistry::lookup)
      .flatMap { it.handle(update) }
  }

  companion object {
    private val log = LogManager.getLogger()
  }
}

