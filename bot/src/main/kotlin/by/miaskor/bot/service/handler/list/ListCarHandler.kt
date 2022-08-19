package by.miaskor.bot.service.handler.list

import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType.CAR
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.handler.list.ListEntitySender.sendEntities
import by.miaskor.domain.api.connector.CarConnector
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.StoreHouseIdWithLimitRequest
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class ListCarHandler(
  private val carConnector: CarConnector,
  private val listSettings: ListSettings,
  private val telegramClientCache: TelegramClientCache
) : ListHandler<ListEntity> {
  override val listEntityType = CAR

  override fun handle(update: Update, listEntityCache: AbstractListCache<ListEntity>): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .map { telegramClient ->
        StoreHouseIdWithLimitRequest(
          storeHouseId = telegramClient.currentStoreHouseId(),
          limit = listSettings.limit(),
          offset = listEntityCache.getOffset(update.chatId)
        )
      }.flatMap { storeHouseIdRequest -> sendCars(update, storeHouseIdRequest) }
  }

  private fun sendCars(
    update: Update,
    storeHouseIdWithLimitRequest: StoreHouseIdWithLimitRequest
  ): Mono<Unit> {
    return Mono.just(storeHouseIdWithLimitRequest)
      .flatMap(carConnector::getAllByStoreHouseId)
      .flatMap { responseWithLimit ->
        sendEntities(
          update,
          responseWithLimit,
          MessageSettings::listCarMessage,
          CarResponse::disassembly
        )
      }
  }
}
