package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.Advertisement
import by.miaskor.domain.tables.records.AdvertisementRecord
import reactor.core.publisher.Mono

object AdvertisementMapper : EntityMapper<Advertisement, AdvertisementRecord> {
  override fun map(from: Advertisement): Mono<AdvertisementRecord> {
    return Mono.fromSupplier {
      AdvertisementRecord().apply {
        id = null
        article = from.article
        photo = from.photo
        description = from.description
        price = from.price
        quality = from.quality
        currency = from.currency
        carPartId = from.carPartId
        salePercent = from.salePercent
        partNumber = from.partNumber
        carId = from.carId
        active = from.active
      }
    }
  }
}
