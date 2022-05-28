package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.domain.ListEntity
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.CarResponse
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Duration

class ListEntityCache(
  private val cacheSettings: CacheSettings,
  private val listSettings: ListSettings
) {
  private val cache: Cache<Long, ListEntity> = Caffeine.newBuilder()
    .expireAfterWrite(Duration.parse(cacheSettings.entityTtl()))
    .maximumSize(cacheSettings.size())
    .build()

  fun populate(chatId: Long, listEntity: ListEntity = ListEntity(listSettings.offset(), listSettings.offset())) {
    cache.put(chatId, listEntity)
  }

  fun getCarOffset(chatId: Long): Long {
    return cache.getIfPresent(chatId)?.carOffset ?: listSettings.offset()
  }

  fun getAutoPartOffset(chatId: Long): Long {
    return cache.getIfPresent(chatId)?.autoPartOffset ?: listSettings.offset()
  }

  fun isExists(chatId: Long): Boolean {
    return cache.getIfPresent(chatId) != null
  }

  fun nextAutoParts(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId)
    populate(
      chatId,
      listEntity?.copy(autoPartOffset = listEntity.autoPartOffset + listSettings.limit())
        ?: ListEntity(listSettings.offset(), listSettings.offset())
    )
  }

  fun nextCars(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId)
    if (listEntity?.reachLimitCars == true) return
    populate(
      chatId,
      listEntity?.copy(carOffset = listEntity.carOffset + listSettings.limit())
        ?: ListEntity(listSettings.offset(), listSettings.offset())
    )
  }

  fun prevAutoParts(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId)
    if (listEntity?.reachLimitAutoParts == true) return
    val autoPartOffset = (listEntity?.autoPartOffset ?: 0) - listSettings.limit()
    val calculatedAutoPartOffset = if (autoPartOffset < 1) 0 else autoPartOffset
    populate(
      chatId,
      listEntity?.copy(autoPartOffset = calculatedAutoPartOffset, reachLimitAutoParts = false)
        ?: ListEntity(listSettings.limit(), listSettings.offset())
    )
  }

  fun prevCars(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId)
    val carOffset = (listEntity?.carOffset ?: 0) - listSettings.limit()
    val calculatedCarOffset = if (carOffset < 1) 0 else carOffset
    populate(
      chatId,
      listEntity?.copy(carOffset = calculatedCarOffset, reachLimitCars = false)
        ?: ListEntity(listSettings.limit(), listSettings.offset())
    )
  }

  fun <T> reachLimit(chatId: Long, entity: T) {
    val listEntity = cache.getIfPresent(chatId)
    when (entity) {
      is CarResponse ->
        populate(
          chatId,
          listEntity?.copy(reachLimitCars = true)
            ?: ListEntity(listSettings.limit(), listSettings.offset())
        )

      is AutoPartResponse ->
        populate(
          chatId,
          listEntity?.copy(reachLimitAutoParts = true)
            ?: ListEntity(listSettings.limit(), listSettings.offset())
        )
    }
  }

  fun evict(chatId: Long) {
    cache.invalidate(chatId)
  }
}
