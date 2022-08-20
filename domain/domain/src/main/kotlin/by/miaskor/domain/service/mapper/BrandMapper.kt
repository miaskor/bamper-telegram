package by.miaskor.domain.service.mapper

import by.miaskor.domain.api.domain.BrandResponse
import by.miaskor.domain.tables.pojos.Brand

class BrandMapper {

  fun map(brand: Brand): BrandResponse {
    return BrandResponse(
      id = brand.id ?: -1,
      brandName = brand.brandName ?: "",
      model = brand.model ?: ""
    )
  }
}
