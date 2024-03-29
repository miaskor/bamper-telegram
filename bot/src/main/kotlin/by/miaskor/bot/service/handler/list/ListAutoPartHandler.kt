package by.miaskor.bot.service.handler.list

import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType.AUTO_PART
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.StoreHouseIdWithLimitRequest
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ListAutoPartHandler(
  private val listSettings: ListSettings,
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache
) : ListHandler<ListEntity> {

  override val listEntityType = AUTO_PART

  override fun handle(update: Update, listEntityCache: AbstractListCache<ListEntity>): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { telegramClient ->
        val storeHouseIdWithLimitRequest = StoreHouseIdWithLimitRequest(
          storeHouseId = telegramClient.currentStoreHouseId(),
          limit = listSettings.limit(),
          offset = listEntityCache.getOffset(update.chatId)
        )
        sendAutoParts(update, storeHouseIdWithLimitRequest, telegramClient)
      }
  }

  private fun sendAutoParts(
    update: Update,
    storeHouseIdWithLimitRequest: StoreHouseIdWithLimitRequest,
    telegramClient: TelegramClient
  ): Mono<Unit> {
    return Mono.just(storeHouseIdWithLimitRequest)
      .flatMap(autoPartConnector::getAllByStoreHouseId)
      .flatMap { responseWithLimit ->
        ListEntitySender.sendEntities(
          update,
          responseWithLimit,
          MessageSettings::listAutoPartMessage
        ) { AutoPartWithPhotoResponse.disassembly(it, telegramClient.chatLanguage) }
      }
  }
}
