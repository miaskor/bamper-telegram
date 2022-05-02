package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.SparePart
import by.miaskor.domain.tables.records.SparePartRecord
import reactor.core.publisher.Mono

object SparePartMapper : EntityMapper<SparePart, SparePartRecord> {
  override fun map(from: SparePart): Mono<SparePartRecord> {
    return Mono.fromSupplier {
      SparePartRecord().apply {
        id = null
        price = from.price
        photo = from.photo
        description = from.description
        currency = from.currency
        carPartId = from.carPartId
        quality = from.quality
        carId = from.carId
        partNumber = from.partNumber
        storeHouseId = from.storeHouseId
      }
    }
  }
}
