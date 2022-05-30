package by.miaskor.domain.service

import by.miaskor.domain.api.domain.WorkerTelegramStoreHouseDto
import by.miaskor.domain.model.WorkerStoreHouseVO
import by.miaskor.domain.repository.WorkerStoreHouseRepository
import by.miaskor.domain.tables.pojos.WorkerStoreHouse
import reactor.core.publisher.Mono

class WorkerStoreHouseService(
  private val workerStoreHouseRepository: WorkerStoreHouseRepository
) {

  fun create(workerTelegramStoreHouseDto: WorkerTelegramStoreHouseDto): Mono<Unit> {
    return Mono.fromSupplier {
      WorkerStoreHouse(
        workerTelegramClientId = workerTelegramStoreHouseDto.employeeTelegramId,
        storeHouseId = workerTelegramStoreHouseDto.storeHouseId,
        workerPrivilege = workerTelegramStoreHouseDto.privilege
      )
    }.flatMap(workerStoreHouseRepository::save)
  }

  fun getStoreHousesByChatId(chatId: Long): Mono<List<WorkerStoreHouseVO>> {
    return workerStoreHouseRepository.findStoreHousesByChatId(chatId)
  }

  fun removeByChatId(chatId: Long): Mono<Unit> {
    return workerStoreHouseRepository.removeByChatId(chatId)
  }
}
