package by.miaskor.bot.service.cache

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType
import by.miaskor.bot.service.extension.chatId
import com.pengrad.telegrambot.model.Update

class AutoPartListEntityCache(
  listSettings: ListSettings,
  cacheSettings: CacheSettings
) : AbstractListCache<ListEntity>(
  cacheSettings,
  listSettings
) {
  override fun populateIsNotExists(update: Update) {
    if (!isExists(update.chatId))
      populate(update.chatId, ListEntity())
  }

  override fun copyEntity(listEntity: ListEntity, offset: Long, reachLimit: Boolean): ListEntity {
    return ListEntity(
      offset = offset,
      reachLimit = reachLimit
    )
  }

  override fun listEntityType() = ListEntityType.AUTO_PART
}