package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.domain.api.connector.TelegramClientConnector
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

class TelegramClientCache(
  private val telegramClientConnector: TelegramClientConnector
) {

  private val cache: Cache<Long, TelegramClient> = Caffeine.newBuilder()
    .expireAfterWrite(1, TimeUnit.HOURS)
    .maximumSize(1000)
    .build()

  fun populate(chatId: Long, telegramClient: TelegramClient) {
    cache.put(chatId, telegramClient)
  }

  fun getTelegramClient(chatId: Long): Mono<TelegramClient> {
    return Mono.justOrEmpty(cache.getIfPresent(chatId))
      .switchIfEmpty(Mono.defer { isClientExists(chatId) })
      .defaultIfEmpty(
        TelegramClient(
          chatId = chatId,
          botState = BotState.GREETINGS
        )
      ).doOnNext { populate(chatId, it) }
  }

  private fun isClientExists(chatId: Long): Mono<TelegramClient> {
    return telegramClientConnector.getByChatId(chatId)
      .filter { it.chatId > 0 }
      .map {
        TelegramClient(
          chatId = it.chatId,
          chatLanguage = it.chatLanguage,
          bamperClientId = it.bamperClientId,
          botState = BotState.MAIN_MENU
        )
      }.doOnNext { populate(chatId, it) }
  }
}
