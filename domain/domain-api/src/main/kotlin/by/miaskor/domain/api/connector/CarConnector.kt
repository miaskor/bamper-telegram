package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdWithLimitRequest
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val BASE_URI = "/cars"

class CarConnector(
  private val webClient: WebClient,
) {

  fun create(carDto: CarDto): Mono<Long> {
    return webClient.post()
      .uri(BASE_URI)
      .body(BodyInserters.fromValue(carDto))
      .retrieve()
      .bodyToMono()
  }

  fun getByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<CarResponse> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("storeHouseId", storeHouseId)
          .queryParam("id", id)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Boolean> {
    return webClient.delete()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("storeHouseId", storeHouseId)
          .queryParam("id", id)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun getAllByStoreHouseId(storeHouseIdWithLimitRequest: StoreHouseIdWithLimitRequest): Mono<ResponseWithLimit<CarResponse>> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("storeHouseId", storeHouseIdWithLimitRequest.storeHouseId)
          .queryParam("limit", storeHouseIdWithLimitRequest.limit)
          .queryParam("offset", storeHouseIdWithLimitRequest.offset)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }
}
