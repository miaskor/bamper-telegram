package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.CarDto
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
}
