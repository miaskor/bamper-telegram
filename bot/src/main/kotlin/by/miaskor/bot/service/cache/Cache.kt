package by.miaskor.bot.service.cache

import by.miaskor.bot.configuration.settings.CacheSettings
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration

open class Cache<K, V>(
  cacheSettings: CacheSettings,
) {

  val cache: Cache<K, V> = Caffeine.newBuilder()
    .expireAfterWrite(Duration.parse(cacheSettings.entityTtl()))
    .maximumSize(cacheSettings.size())
    .build()

  open fun populate(key: K, value: V): V {
    cache.put(key, value)
    return value
  }

  open fun get(key: K, defValue: V): V {
    return cache.getIfPresent(key) ?: populate(key, defValue)
  }

  open fun isExists(key: K): Boolean {
    return cache.getIfPresent(key) != null
  }

  open fun evict(key: K) {
    cache.invalidate(key)
  }
}
