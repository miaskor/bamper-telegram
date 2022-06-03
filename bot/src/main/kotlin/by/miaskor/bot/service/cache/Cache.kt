package by.miaskor.bot.service.cache

import by.miaskor.bot.configuration.settings.CacheSettings
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration

abstract class Cache<K, V>(
  cacheSettings: CacheSettings
) {

  val cache: Cache<K, V> = Caffeine.newBuilder()
    .expireAfterWrite(Duration.parse(cacheSettings.entityTtl()))
    .maximumSize(cacheSettings.size())
    .build()

  open fun populate(key: K, value: V) {
    cache.put(key, value)
  }

  open fun isExists(key: K): Boolean {
    return cache.getIfPresent(key) != null
  }

  open fun evict(key: K) {
    cache.invalidate(key)
  }
}