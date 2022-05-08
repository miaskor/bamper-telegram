package by.miaskor.domain.service

import by.miaskor.domain.api.domain.EmployeeExistsRequest
import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.repository.TelegramClientRepository
import by.miaskor.domain.tables.pojos.TelegramClient
import reactor.core.publisher.Mono

class TelegramClientService(
  private val workerTelegramService: WorkerTelegramService,
  private val telegramClientRepository: TelegramClientRepository
) {

  fun upsert(telegramClientRequest: TelegramClientRequest): Mono<Unit> {
    return telegramClientRepository.save(
      TelegramClient(
        chatId = telegramClientRequest.chatId,
        chatLanguage = telegramClientRequest.chatLanguage,
        userName = telegramClientRequest.username,
      )
    )
  }

  fun getByChatId(chatId: Long): Mono<TelegramClientResponse> {
    return telegramClientRepository.findByChatId(chatId)
      .map {
        TelegramClientResponse(
          it.chatId ?: -1,
          it.chatLanguage ?: "",
          it.bamperClientId
        )
      }
  }

  fun getByUsername(username: String): Mono<TelegramClientResponse> {
    return telegramClientRepository.findByUsername(username)
      .map {
        TelegramClientResponse(
          it.chatId ?: -1,
          it.chatLanguage ?: "",
          it.bamperClientId
        )
      }
  }

  fun isTelegramClientWorker(employeeExistsRequest: EmployeeExistsRequest): Mono<Boolean> {
    return Mono.just(employeeExistsRequest.employeeUsername)
      .flatMap(::getByUsername)
      .flatMap {
        workerTelegramService.getByWorkerChatIdAndEmployerChatId(it.chatId, employeeExistsRequest.employerChatId)
          .hasElement()
      }.defaultIfEmpty(false)
  }
}
