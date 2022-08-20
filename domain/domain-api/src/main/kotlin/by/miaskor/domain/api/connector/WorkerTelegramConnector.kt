package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.WorkerTelegramDto
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val BASE_URI = "/worker-telegrams"

class WorkerTelegramConnector(
  private val webClient: WebClient,
) {

  fun create(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return webClient.post()
      .uri(BASE_URI)
      .bodyValue(workerTelegramDto)
      .retrieve()
      .bodyToMono()
  }

  fun find(workerTelegramDto: WorkerTelegramDto): Mono<WorkerTelegramDto> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("employeeChatId", workerTelegramDto.employeeChatId)
          .queryParam("employerChatId", workerTelegramDto.employerChatId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun remove(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return webClient.delete()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("employeeChatId", workerTelegramDto.employeeChatId)
          .queryParam("employerChatId", workerTelegramDto.employerChatId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }
}
