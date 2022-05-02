package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.Brand
import by.miaskor.domain.tables.records.BrandRecord
import reactor.core.publisher.Mono

object BrandMapper : EntityMapper<Brand, BrandRecord> {
  override fun map(from: Brand): Mono<BrandRecord> {
    return Mono.fromSupplier {
      BrandRecord().apply {
        id = null
        brandName = from.brandName
        model = from.model
        yearManufactureFrom = from.yearManufactureFrom
        yearManufactureTo = from.yearManufactureTo
      }
    }
  }
}
