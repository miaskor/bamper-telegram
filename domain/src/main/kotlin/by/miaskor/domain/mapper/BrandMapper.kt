package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.Brand
import by.miaskor.domain.tables.records.BrandRecord

object BrandMapper : EntityMapper<Brand, BrandRecord> {
  override fun map(from: Brand): BrandRecord {
    return BrandRecord().apply {
      id = null
      brandName = from.brandName
      model = from.model
      yearManufactureFrom = from.yearManufactureFrom
      yearManufactureTo = from.yearManufactureTo
    }
  }
}
