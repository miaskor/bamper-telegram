package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.BrandResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val BASE_URI = "/brands"

class BrandConnector(
  private val webClient: WebClient,
) {

  fun getByBrandName(brandName: String): Mono<BrandResponse> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("brandName", brandName)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun getByBrandNameAndModel(brandName: String, model: String): Mono<BrandResponse> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("brandName", brandName)
        uriBuilder.queryParam("model", model)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }
}
