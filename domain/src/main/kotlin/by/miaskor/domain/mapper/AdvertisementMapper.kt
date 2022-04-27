package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.Advertisement
import by.miaskor.domain.tables.records.AdvertisementRecord

object AdvertisementMapper : EntityMapper<Advertisement, AdvertisementRecord> {
  override fun map(from: Advertisement): AdvertisementRecord {
    return AdvertisementRecord().apply {
      id = null
      article = from.article
      photo = from.photo
      description = from.description
      price = from.price
      quality = from.quality
      currency = from.currency
      partNumber = from.partNumber
      carPartId = from.carPartId
      carId = from.carId
    }
  }
}
