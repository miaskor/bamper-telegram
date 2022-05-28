package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class AutoPartConnector(
  private val webClient: WebClient
) {

  fun create(autoPartDto: AutoPartDto): Mono<Unit> {
    return webClient.post()
      .uri("/auto-part")
      .body(BodyInserters.fromValue(autoPartDto))
      .retrieve()
      .bodyToMono()
  }

  fun getAllByStoreHouseId(storeHouseIdRequest: StoreHouseIdRequest): Mono<ResponseWithLimit<AutoPartResponse>> {
    return webClient.post()
      .uri("/auto-part/list")
      .body(BodyInserters.fromValue(storeHouseIdRequest))
      .retrieve()
      .bodyToMono()
  }
}
