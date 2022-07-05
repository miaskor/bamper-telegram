package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.BamperClientDto
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class BamperClientConnector(
  private val webClient: WebClient,
) {

  fun create(bamperClientDto: BamperClientDto): Mono<Unit> {
    return webClient.post()
      .uri("/bamper-client/create")
      .body(BodyInserters.fromValue(bamperClientDto))
      .retrieve()
      .bodyToMono()
  }
}
