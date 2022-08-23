package by.miaskor.bot.telegram

import by.miaskor.bot.configuration.settings.ExecutorSettings
import by.miaskor.bot.service.CommandResolver.processCommand
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.info
import by.miaskor.common.BindingProperty
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class Bot(
  telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache,
) {
  private val executorSettings: ExecutorSettings by BindingProperty("bot.executor")

  init {
    telegramBot.setUpdatesListener { updates ->
      Flux.fromIterable(updates)
        .doOnNext {
          log.info("Processing update=${it.info()}")
        }
        .parallel(executorSettings.maxConcurrency())
        .runOn(Schedulers.newParallel("telegram-bot-thread", executorSettings.maxConcurrency()))
        .flatMap(::processChatId)
        .subscribe()

      CONFIRMED_UPDATES_ALL
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

