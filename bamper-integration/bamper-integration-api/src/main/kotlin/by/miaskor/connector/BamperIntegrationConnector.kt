package by.miaskor.connector

import by.miaskor.domain.AuthDto
import by.miaskor.domain.ImportAdvertisementRequest
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class BamperIntegrationConnector(
  private val webClient: WebClient,
) {

  fun auth(authDto: AuthDto): Mono<String> {
    return webClient.post()
      .uri("/bamper/auth")
      .body(BodyInserters.fromValue(authDto))
      .retrieve()
      .bodyToMono()
  }

  fun importAdvertisement(importAdvertisementRequest: ImportAdvertisementRequest): Mono<Unit> {
    return webClient.post()
      .uri("/bamper/import")
      .body(BodyInserters.fromValue(importAdvertisementRequest))
      .retrieve()
      .bodyToMono()
  }
}
