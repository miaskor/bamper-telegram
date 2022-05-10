package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.WorkerTelegramDto
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class WorkerTelegramConnector(
  private val webClient: WebClient
) {

  fun create(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return webClient.post()
      .uri("/worker-telegram")
      .bodyValue(workerTelegramDto)
      .retrieve()
      .bodyToMono()
  }

  fun find(workerTelegramDto: WorkerTelegramDto): Mono<WorkerTelegramDto> {
    return webClient.post()
      .uri("/worker-telegram/find")
      .bodyValue(workerTelegramDto)
      .retrieve()
      .bodyToMono()
  }

  fun remove(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return webClient.delete()
      .uri("/worker-telegram/${workerTelegramDto.employerChatId}/${workerTelegramDto.employeeChatId}")
      .retrieve()
      .bodyToMono()
  }
}
