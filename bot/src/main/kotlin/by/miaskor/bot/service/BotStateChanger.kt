package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.Companion.isNotFinalState
import by.miaskor.bot.service.cache.ListEntityCacheRegistry
import by.miaskor.bot.service.cache.TelegramClientCache
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Mono

object BotStateChanger {

  lateinit var telegramClientCache: TelegramClientCache
  lateinit var listEntityCacheRegistry: ListEntityCacheRegistry
  private val log = LogManager.getLogger()

  fun <T : Any> Mono<T>.changeBotState(
    chatId: () -> Long,
    botState: BotState,
    isBackCommand: Boolean = false
  ): Mono<T> {
    return this.flatMap { t ->
      Mono.fromSupplier { chatId.invoke() }
        .flatMap(telegramClientCache::getTelegramClient)
        .doOnNext { log.info("Change botState for=$it") }
        .doOnNext { telegramClient ->
          listEntityCacheRegistry.evictAll(telegramClient.chatId)
          telegramClientCache.populate(
            telegramClient.chatId,
            telegramClient.copy(currentBotState = botState).apply {
              if (isNotFinalState(telegramClient.currentBotState) && !isBackCommand) {
                this.previousBotStates.add(telegramClient.currentBotState)
              }
            }
          )
        }.then(Mono.just(t))
    }
  }
}
