package by.miaskor.bot.telegram

import by.miaskor.bot.service.CommandResolver.processCommand
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.info
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class Bot(
  telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache,
) {

  init {
    telegramBot.setUpdatesListener { updates ->
      Flux.fromIterable(updates)
        .doOnNext {
          log.info("Processing update=${it.info()}")
        }
        .flatMap(::processChatId)
        .then(Mono.just(CONFIRMED_UPDATES_ALL))
        .toFuture()
        .get()
    }
  }

  private fun processChatId(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .map { it.currentBotState }
      .processCommand(update)
  }

  companion object {
    private val log = LogManager.getLogger()
  }
}

