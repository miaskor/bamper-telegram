package by.miaskor.bot.service.cache

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.domain.api.connector.TelegramClientConnector
import reactor.core.publisher.Mono
import java.util.*

class TelegramClientCache(
  private val telegramClientConnector: TelegramClientConnector,
  cacheSettings: CacheSettings
) : Cache<Long, TelegramClient>(cacheSettings) {

  fun updateChatLanguage(chatId: Long, chatLanguage: String) {
    Optional.ofNullable(cache.getIfPresent(chatId))
      .ifPresent { cache.put(chatId, it.copy(chatLanguage = chatLanguage)) }
  }

  fun getTelegramClient(chatId: Long): Mono<TelegramClient> {
    return Mono.justOrEmpty(cache.getIfPresent(chatId))
      .switchIfEmpty(Mono.defer { isClientExists(chatId) })
      .defaultIfEmpty(TelegramClient(chatId = chatId))
      .doOnNext { populate(chatId, it) }
  }

  private fun isClientExists(chatId: Long): Mono<TelegramClient> {
    return telegramClientConnector.getByChatId(chatId)
      .map {
        TelegramClient(
          chatId = it.chatId,
          chatLanguage = it.chatLanguage,
          bamperClientId = it.bamperClientId,
          currentBotState = MAIN_MENU
        )
      }.doOnNext { populate(chatId, it) }
  }
}
