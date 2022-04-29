package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.StoreHouse
import by.miaskor.domain.tables.records.StoreHouseRecord
import reactor.core.publisher.Mono

object StoreHouseMapper : EntityMapper<StoreHouse, StoreHouseRecord> {
  override fun map(from: StoreHouse): Mono<StoreHouseRecord> {
    return Mono.fromSupplier {
      StoreHouseRecord().apply {
        id = null
        storeHouseName = from.storeHouseName
      }
    }
  }
}
