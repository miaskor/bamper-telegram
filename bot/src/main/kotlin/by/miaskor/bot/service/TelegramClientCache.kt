package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.BotState.GREETINGS
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.domain.api.connector.TelegramClientConnector
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

class TelegramClientCache(
  private val telegramClientConnector: TelegramClientConnector,
  cacheSettings: CacheSettings
) {

  private val cache: Cache<Long, TelegramClient> = Caffeine.newBuilder()
    .expireAfterWrite(Duration.parse(cacheSettings.entityTtl()))
    .maximumSize(cacheSettings.size())
    .build()

  fun populate(chatId: Long, telegramClient: TelegramClient) {
    cache.put(chatId, telegramClient)
  }

  fun updateChatLanguage(chatId: Long, chatLanguage: String) {
    Optional.ofNullable(cache.getIfPresent(chatId))
      .ifPresent { cache.put(chatId, it.copy(chatLanguage = chatLanguage)) }
  }

  fun updateBotState(chatId: Long, botState: BotState) {
    Optional.ofNullable(cache.getIfPresent(chatId))
      .ifPresent { cache.put(chatId, it.copy(botState = botState)) }
  }

  fun getTelegramClient(chatId: Long): Mono<TelegramClient> {
    return Mono.justOrEmpty(cache.getIfPresent(chatId))
      .switchIfEmpty(Mono.defer { isClientExists(chatId) })
      .defaultIfEmpty(
        TelegramClient(
          chatId = chatId,
          botState = GREETINGS
        )
      ).doOnNext { populate(chatId, it) }
  }

  private fun isClientExists(chatId: Long): Mono<TelegramClient> {
    return telegramClientConnector.getByChatId(chatId)
      .map {
        TelegramClient(
          chatId = it.chatId,
          chatLanguage = it.chatLanguage,
          bamperClientId = it.bamperClientId,
          botState = MAIN_MENU
        )
      }.doOnNext { populate(chatId, it) }
  }
}
