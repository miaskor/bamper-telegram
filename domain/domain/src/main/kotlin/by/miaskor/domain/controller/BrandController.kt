package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.BrandResponse
import by.miaskor.domain.service.BrandService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/brands")
class BrandController(
  private val brandService: BrandService,
) {

  @GetMapping(params = ["brandName"])
  fun getByBrandName(@RequestParam brandName: String): Mono<ResponseEntity<BrandResponse>> {
    return Mono.just(brandName)
      .flatMap(brandService::getByBrandName)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping(params = ["brandName", "model"])
  fun getByBrandNameAndModel(
    @RequestParam brandName: String,
    @RequestParam model: String,
  ): Mono<ResponseEntity<BrandResponse>> {
    return brandService.getByBrandNameAndModel(brandName, model)
      .map { ResponseEntity.ok(it) }
  }
}
