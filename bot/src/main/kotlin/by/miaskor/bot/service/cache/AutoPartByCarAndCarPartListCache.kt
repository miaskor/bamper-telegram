package by.miaskor.bot.service.cache

import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.domain.ConstraintAutoPartListEntity
import by.miaskor.bot.domain.ListEntityType
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.text
import by.miaskor.domain.api.domain.ConstraintType
import com.pengrad.telegrambot.model.Update

class AutoPartByCarAndCarPartListCache(
  listSettings: ListSettings,
  cacheSettings: CacheSettings,
) : AbstractListCache<ConstraintAutoPartListEntity>(
  cacheSettings,
  listSettings
) {
  override fun populateIsNotExists(update: Update) {
    if (!isExists(update.chatId))
      populate(
        update.chatId,
        ConstraintAutoPartListEntity(constraintType = ConstraintType.CAR_AND_CAR_PART, constraint = update.text)
      )
  }

  override fun copyEntity(
    listEntity: ConstraintAutoPartListEntity,
    offset: Long,
    reachLimit: Boolean,
  ): ConstraintAutoPartListEntity {
    return listEntity.copy(
      offset = offset,
      reachLimit = reachLimit
    )
  }

  override fun listEntityType() = ListEntityType.FIND_AUTO_PART
}
