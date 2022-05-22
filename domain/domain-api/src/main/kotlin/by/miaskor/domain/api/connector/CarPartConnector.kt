package by.miaskor.domain.api.connector

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class CarPartConnector(
  private val webClient: WebClient
) {

  fun getIdByName(name: String): Mono<Long> {
    return webClient.get()
      .uri("/car-part/$name")
      .retrieve()
      .bodyToMono()
  }
}
