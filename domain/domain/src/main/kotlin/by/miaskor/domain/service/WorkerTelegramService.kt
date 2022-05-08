package by.miaskor.domain.service

import by.miaskor.domain.api.domain.WorkerTelegramRequest
import by.miaskor.domain.repository.WorkerTelegramRepository
import by.miaskor.domain.tables.pojos.WorkerTelegram
import reactor.core.publisher.Mono

class WorkerTelegramService(
  private val workerTelegramRepository: WorkerTelegramRepository
) {

  fun getByWorkerChatIdAndEmployerChatId(workerChatId: Long, employerChatId: Long): Mono<WorkerTelegram> {
    return workerTelegramRepository.findByWorkerChatIdAndEmployerChatId(workerChatId, employerChatId)
  }

  fun save(workerTelegramRequest: WorkerTelegramRequest): Mono<Unit> {
    return Mono.just(workerTelegramRequest)
      .map {
        WorkerTelegram(
          workerTelegramChatId = workerTelegramRequest.employeeChatId,
          employerTelegramChatId = workerTelegramRequest.employerChatId
        )
      }.flatMap(workerTelegramRepository::save)
  }
}
