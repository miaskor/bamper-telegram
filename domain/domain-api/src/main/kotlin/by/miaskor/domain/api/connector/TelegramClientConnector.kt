package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val BASE_URI = "/telegram-clients"

class TelegramClientConnector(
  private val webClient: WebClient,
) {

  fun upsert(telegramClientRequest: TelegramClientRequest): Mono<Unit> {
    return webClient.post()
      .uri(BASE_URI)
      .bodyValue(telegramClientRequest)
      .retrieve()
      .bodyToMono()
  }

  fun getByChatId(chatId: Long): Mono<TelegramClientResponse> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("chatId", chatId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun getAllEmployeesByEmployerChatId(employerChatId: Long): Mono<List<TelegramClientResponse>> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("employerChatId", employerChatId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun getByUsername(username: String): Mono<TelegramClientResponse> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("username", username)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }
}
