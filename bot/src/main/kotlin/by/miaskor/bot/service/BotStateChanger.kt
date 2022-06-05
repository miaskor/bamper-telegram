package by.miaskor.bot.service

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.Companion.isNotFinalState
import by.miaskor.bot.domain.BotState.MODIFICATION_STORE_HOUSE_MENU
import by.miaskor.bot.domain.BotState.READ_ONLY_STORE_HOUSE_MENU
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.cache.ListEntityCacheRegistry
import by.miaskor.bot.service.cache.TelegramClientCache
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Mono

object BotStateChanger {

  lateinit var telegramClientCache: TelegramClientCache
  lateinit var listEntityCacheRegistry: ListEntityCacheRegistry
  lateinit var carBuilderCache: Cache<Long, AbstractStepBuilder<CreatingCarStep>>
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
          if (botState == READ_ONLY_STORE_HOUSE_MENU && isBackCommand)
            listEntityCacheRegistry.evictAll(telegramClient.chatId)
          if (botState == MODIFICATION_STORE_HOUSE_MENU && isBackCommand)
            carBuilderCache.evict(telegramClient.chatId)
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
