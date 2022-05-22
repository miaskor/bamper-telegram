package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.BrandDto
import by.miaskor.domain.service.BrandService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/brand")
class BrandController(
  private val brandService: BrandService
) {

  @GetMapping("/brand-name/{brand_name}")
  fun getByBrandName(@PathVariable("brand_name") brandName: String): Mono<ResponseEntity<BrandDto>> {
    return Mono.just(brandName)
      .flatMap(brandService::getByBrandName)
      .map { ResponseEntity.ok(it) }
  }

  @PostMapping
  fun getByBrandNameAndModel(@RequestBody brandDto: BrandDto): Mono<ResponseEntity<BrandDto>> {
    return Mono.just(brandDto)
      .flatMap(brandService::getByBrandNameAndModel)
      .map { ResponseEntity.ok(it) }
  }
}
