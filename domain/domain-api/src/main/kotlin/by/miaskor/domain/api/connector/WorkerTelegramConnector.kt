package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.WorkerTelegramRequest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class WorkerTelegramConnector(
  private val webClient: WebClient
) {

  fun create(workerTelegramRequest: WorkerTelegramRequest): Mono<Unit> {
    return webClient.post()
      .uri("/worker-telegram")
      .bodyValue(workerTelegramRequest)
      .retrieve()
      .bodyToMono()
  }
}
