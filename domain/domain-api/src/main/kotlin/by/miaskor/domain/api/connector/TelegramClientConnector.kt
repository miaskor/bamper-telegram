package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class TelegramClientConnector(
  private val webClient: WebClient
) {

  fun create(telegramClientRequest: TelegramClientRequest): Mono<Unit> {
    return webClient.post()
      .uri("/telegram-client")
      .bodyValue(telegramClientRequest)
      .retrieve()
      .bodyToMono()
  }

  fun getByChatId(chatId: Long): Mono<TelegramClientResponse> {
    return webClient.get()
      .uri("/telegram-client/$chatId")
      .retrieve()
      .bodyToMono()
  }
}
