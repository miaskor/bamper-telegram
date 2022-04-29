package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.TelegramClientStoreHouse
import by.miaskor.domain.tables.records.TelegramClientStoreHouseRecord
import reactor.core.publisher.Mono

object TelegramClientStoreHouseMapper : EntityMapper<TelegramClientStoreHouse, TelegramClientStoreHouseRecord> {
  override fun map(from: TelegramClientStoreHouse): Mono<TelegramClientStoreHouseRecord> {
    return Mono.fromSupplier {
      TelegramClientStoreHouseRecord().apply {
        telegramClientId = from.telegramClientId
        storeHouseId = from.storeHouseId
      }
    }
  }
}
