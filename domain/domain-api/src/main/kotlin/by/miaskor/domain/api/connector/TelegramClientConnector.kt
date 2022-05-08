package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.EmployeeExistsRequest
import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class TelegramClientConnector(
  private val webClient: WebClient
) {

  fun upsert(telegramClientRequest: TelegramClientRequest): Mono<Unit> {
    return webClient.post()
      .uri("/telegram-client")
      .bodyValue(telegramClientRequest)
      .retrieve()
      .bodyToMono()
  }

  fun getByChatId(chatId: Long): Mono<TelegramClientResponse> {
    return webClient.get()
      .uri("/telegram-client/chatId/$chatId")
      .retrieve()
      .bodyToMono()
  }

  fun getAllEmployeesByEmployerChatId(employerChatId: Long): Mono<List<TelegramClientResponse>> {
    return webClient.get()
      .uri("/telegram-client/employees/$employerChatId")
      .retrieve()
      .bodyToMono()
  }

  fun getByUsername(username: String): Mono<TelegramClientResponse> {
    return webClient.get()
      .uri("/telegram-client/username/$username")
      .retrieve()
      .bodyToMono()
  }

  fun isTelegramClientWorker(employeeExistsRequest: EmployeeExistsRequest): Mono<Boolean> {
    return webClient.post()
      .uri("/telegram-client/isWorker")
      .bodyValue(employeeExistsRequest)
      .retrieve()
      .bodyToMono()
  }
}
