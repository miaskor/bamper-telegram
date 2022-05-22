package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.AutoPartDto
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
}
