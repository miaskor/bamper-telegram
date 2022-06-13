package by.miaskor.bot.service.cache

import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType

class ListEntityCacheRegistry(
  vararg caches: AbstractListCache<ListEntity>
) {

  private val mapCaches = caches.associateBy { it.listEntityType() }

  fun lookup(listEntityType: ListEntityType): AbstractListCache<ListEntity> {
    return mapCaches[listEntityType]
      ?: throw IllegalArgumentException("List entity cache with type=$listEntityType didn't found")
  }

  fun evictAll(chatId: Long) {
    mapCaches.values.forEach {
      it.evict(chatId)
    }
  }
}