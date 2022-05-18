package by.miaskor.domain.service

import by.miaskor.domain.api.domain.BrandDto
import by.miaskor.domain.repository.BrandRepository
import reactor.core.publisher.Mono

class BrandService(
  private val brandRepository: BrandRepository
) {

  fun getByBrandName(brandName: String): Mono<BrandDto> {
    return brandRepository.findByBrandName(brandName)
      .map {
        BrandDto(
          brandName = it.brandName ?: "",
          model = it.model ?: ""
        )
      }
  }

  fun getByBrandNameAndModel(brandDto: BrandDto): Mono<BrandDto> {
    return brandRepository.findByBrandNameAndModel(brandDto.brandName, brandDto.model)
      .map {
        BrandDto(
          id = it.id ?: -1,
          brandName = it.brandName ?: "",
          model = it.model ?: ""
        )
      }
  }
}
