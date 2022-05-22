package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.BrandDto
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class BrandConnector(
  private val webClient: WebClient
) {

  fun getByBrandName(brandName: String): Mono<BrandDto> {
    return webClient.get()
      .uri("/brand/brand-name/$brandName")
      .retrieve()
      .bodyToMono()
  }

  fun getByBrandNameAndModel(brandDto: BrandDto): Mono<BrandDto> {
    return webClient.post()
      .uri("/brand")
      .body(BodyInserters.fromValue(brandDto))
      .retrieve()
      .bodyToMono()
  }
}
