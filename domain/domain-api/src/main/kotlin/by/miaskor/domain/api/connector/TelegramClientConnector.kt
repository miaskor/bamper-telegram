package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.TelegramClientRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class TelegramClientConnector(
  private val webClient: WebClient
) {

  fun create(telegramClientRequest: TelegramClientRequest): Mono<ResponseEntity<Unit>> {
    return webClient.post()
      .uri("/telegram-client")
      .bodyValue(telegramClientRequest)
      .retrieve()
      .bodyToMono()
  }
}
