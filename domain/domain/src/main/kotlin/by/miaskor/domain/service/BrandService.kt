package by.miaskor.domain.service

import by.miaskor.domain.api.domain.BrandResponse
import by.miaskor.domain.repository.BrandRepository
import by.miaskor.domain.service.mapper.BrandMapper
import reactor.core.publisher.Mono

class BrandService(
  private val brandRepository: BrandRepository,
  private val brandMapper: BrandMapper,
) {

  fun getByBrandName(brandName: String): Mono<BrandResponse> {
    return brandRepository.findByBrandName(brandName)
      .map(brandMapper::map)
  }

  fun getByBrandNameAndModel(brandName: String, model: String): Mono<BrandResponse> {
    return brandRepository.findByBrandNameAndModel(brandName, model)
      .map(brandMapper::map)
  }
}
