package by.miaskor.bot.service

import by.miaskor.bot.domain.StateBot
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

class TelegramClientCache {

  private val cache: Cache<Long, StateBot> = Caffeine.newBuilder()
    .expireAfterWrite(1, TimeUnit.HOURS)
    .maximumSize(1000)
    .build()

  fun populate(chatId: Long, stateBot: StateBot) {
    cache.put(chatId, stateBot)
  }

  fun getStateBot(chatId: Long): Mono<StateBot> {
    return Mono.just(cache.getIfPresent(chatId) ?: StateBot.CHOOSE_LANGUAGE)
  }
}
