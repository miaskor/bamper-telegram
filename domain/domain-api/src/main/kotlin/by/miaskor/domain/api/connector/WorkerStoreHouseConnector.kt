package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.WorkerTelegramStoreHouseDto
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class WorkerStoreHouseConnector(
  private val webClient: WebClient
) {

  fun create(workerTelegramStoreHouseDto: WorkerTelegramStoreHouseDto): Mono<Unit> {
    return webClient.post()
      .uri("/worker-store-house")
      .body(BodyInserters.fromValue(workerTelegramStoreHouseDto))
      .retrieve()
      .bodyToMono()
  }
}
