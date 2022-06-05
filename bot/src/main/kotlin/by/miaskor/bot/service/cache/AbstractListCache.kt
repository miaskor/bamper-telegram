package by.miaskor.bot.service.cache

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType

abstract class AbstractListCache(
  cacheSettings: CacheSettings,
  private val listSettings: ListSettings
) : Cache<Long, ListEntity>(cacheSettings) {

  fun getOffset(chatId: Long): Long {
    return cache.getIfPresent(chatId)?.offset ?: listSettings.offset()
  }

  fun moveOffset(chatId: Long, callbackCommand: CallbackCommand) {
    if (listEntityType().next() == callbackCommand) {
      nextEntities(chatId)
    } else {
      prevEntities(chatId)
    }
  }

  fun populateIsNotExists(chatId: Long) {
    if (!isExists(chatId))
      populate(chatId, ListEntity())
  }

  private fun nextEntities(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId)
    if (listEntity?.reachLimit == true) return
    populate(
      chatId,
      listEntity?.copy(offset = listEntity.offset + listSettings.limit()) ?: ListEntity()
    )
  }

  private fun prevEntities(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId)
    val carOffset = (listEntity?.offset ?: 0) - listSettings.limit()
    val calculatedCarOffset = if (carOffset < 1) 0 else carOffset
    populate(
      chatId,
      listEntity?.copy(offset = calculatedCarOffset, reachLimit = false) ?: ListEntity()
    )
  }

  abstract fun listEntityType(): ListEntityType
}