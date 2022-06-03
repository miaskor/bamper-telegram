package by.miaskor.bot.service.cache

import by.miaskor.bot.domain.ListEntityType

class ListEntityCacheRegistry(
  vararg caches: AbstractListCache
) {

  private val mapCaches = caches.associateBy { it.listEntityType() }

  fun lookup(listEntityType: ListEntityType): AbstractListCache {
    return mapCaches[listEntityType]
      ?: throw IllegalArgumentException("List entity cache with type=$listEntityType didn't found")
  }

  fun evictAll(chatId: Long) {
    mapCaches.values.forEach {
      it.evict(chatId)
    }
  }
}