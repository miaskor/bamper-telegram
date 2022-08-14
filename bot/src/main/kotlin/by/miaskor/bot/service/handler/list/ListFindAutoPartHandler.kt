package by.miaskor.bot.service.handler.list

import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.ConstraintAutoPartListEntity
import by.miaskor.bot.domain.ListEntityType
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.StoreHouseRequestWithConstraint
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class ListFindAutoPartHandler(
  private val listSettings: ListSettings,
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache,
) : ListHandler<ConstraintAutoPartListEntity> {

  override val listEntityType = ListEntityType.FIND_AUTO_PART

  override fun handle(update: Update, listEntityCache: AbstractListCache<ConstraintAutoPartListEntity>): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .zipWith(Mono.justOrEmpty(listEntityCache.cache.getIfPresent(update.chatId)))
      .flatMap { (telegramClient, autoPart) ->
        val storeHouseIdRequest = StoreHouseRequestWithConstraint(
          constraint = autoPart.constraint,
          storeHouseId = telegramClient.currentStoreHouseId(),
          limit = listSettings.limit(),
          offset = listEntityCache.getOffset(update.chatId),
          constraintType = autoPart.constraintType
        )
        sendAutoParts(update, storeHouseIdRequest, telegramClient)
      }
  }

  private fun sendAutoParts(
    update: Update,
    storeHouseRequestWithConstraint: StoreHouseRequestWithConstraint,
    telegramClient: TelegramClient,
  ): Mono<Unit> {
    return Mono.just(storeHouseRequestWithConstraint)
      .flatMap(autoPartConnector::getAllByConstraint)
      .flatMap { responseWithLimit ->
        ListEntitySender.sendEntities(
          update,
          responseWithLimit,
          MessageSettings::listAutoPartMessage
        ) { AutoPartWithPhotoResponse.disassembly(it, telegramClient.chatLanguage) }
      }
  }
}
