package by.miaskor.domain.service

import by.miaskor.domain.api.domain.BrandResponse
import by.miaskor.domain.repository.BrandRepository
import reactor.core.publisher.Mono

class BrandService(
  private val brandRepository: BrandRepository,
) {

  fun getByBrandName(brandName: String): Mono<BrandResponse> {
    return brandRepository.findByBrandName(brandName)
      .map { brand ->
        BrandResponse(
          id = brand.id ?: -1,
          brandName = brand.brandName ?: "",
          model = brand.model ?: ""
        )
      }
  }

  fun getByBrandNameAndModel(brandName: String, model: String): Mono<BrandResponse> {
    return brandRepository.findByBrandNameAndModel(brandName, model)
      .map { brand ->
        BrandResponse(
          id = brand.id ?: -1,
          brandName = brand.brandName ?: "",
          model = brand.model ?: ""
        )
      }
  }
}
