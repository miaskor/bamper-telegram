package by.miaskor.bot.service.cache

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType
import com.pengrad.telegrambot.model.Update

abstract class AbstractListCache<T : ListEntity>(
  cacheSettings: CacheSettings,
  private val listSettings: ListSettings
) : Cache<Long, T>(cacheSettings) {

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

  private fun nextEntities(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId)
    if (listEntity?.reachLimit == true || listEntity == null) return
    val calculatedOffset = listEntity.offset + listSettings.offset()
    populate(
      chatId,
      copyEntity(listEntity, calculatedOffset, listEntity.reachLimit)
    )
  }

  private fun prevEntities(chatId: Long) {
    val listEntity = cache.getIfPresent(chatId) ?: return
    val offset = listEntity.offset - listSettings.limit()
    val calculatedOffset = if (offset < 1) 0 else offset
    populate(
      chatId,
      copyEntity(listEntity, calculatedOffset, false)
    )
  }

  abstract fun populateIsNotExists(update: Update)

  protected abstract fun copyEntity(listEntity: T, offset: Long, reachLimit: Boolean): T

  abstract fun listEntityType(): ListEntityType
}