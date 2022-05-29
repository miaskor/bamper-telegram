package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class CarConnector(
  private val webClient: WebClient
) {

  fun create(carDto: CarDto): Mono<Long> {
    return webClient.post()
      .uri("/car")
      .body(BodyInserters.fromValue(carDto))
      .retrieve()
      .bodyToMono()
  }

  fun getByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<CarResponse> {
    return webClient.get()
      .uri("/car/$storeHouseId/$id")
      .retrieve()
      .bodyToMono()
  }

  fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Boolean> {
    return webClient.delete()
      .uri("/car/$storeHouseId/$id")
      .retrieve()
      .bodyToMono()
  }

  fun getAllByStoreHouseId(storeHouseIdRequest: StoreHouseIdRequest): Mono<ResponseWithLimit<CarResponse>> {
    return webClient.post()
      .uri("/car/list")
      .body(BodyInserters.fromValue(storeHouseIdRequest))
      .retrieve()
      .bodyToMono()
  }
}
