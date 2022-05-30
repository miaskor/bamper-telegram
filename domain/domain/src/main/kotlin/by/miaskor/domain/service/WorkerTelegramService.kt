package by.miaskor.domain.service

import by.miaskor.domain.api.domain.WorkerTelegramDto
import by.miaskor.domain.repository.WorkerTelegramRepository
import by.miaskor.domain.tables.pojos.WorkerTelegram
import reactor.core.publisher.Mono

class WorkerTelegramService(
  private val workerTelegramRepository: WorkerTelegramRepository,
  private val workerStoreHouseService: WorkerStoreHouseService
) {

  fun get(workerTelegramDto: WorkerTelegramDto): Mono<WorkerTelegramDto> {
    return workerTelegramRepository.find(workerTelegramDto.employeeChatId, workerTelegramDto.employerChatId)
      .map {
        WorkerTelegramDto(
          employeeChatId = it.workerTelegramChatId ?: -1,
          employerChatId = it.employerTelegramChatId ?: -1
        )
      }
  }

  fun remove(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return workerTelegramRepository.remove(workerTelegramDto)
      .then(workerStoreHouseService.removeByChatId(workerTelegramDto.employeeChatId))
  }

  fun getAllWorkerChatIdByEmployerChatId(employerChatId: Long): Mono<List<Long>> {
    return workerTelegramRepository.findAllWorkerChatIdByEmployerChatId(employerChatId)
  }

  fun save(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return Mono.just(workerTelegramDto)
      .map {
        WorkerTelegram(
          workerTelegramChatId = workerTelegramDto.employeeChatId,
          employerTelegramChatId = workerTelegramDto.employerChatId
        )
      }.flatMap(workerTelegramRepository::save)
  }
}
