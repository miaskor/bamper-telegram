package by.miaskor.domain.service

import by.miaskor.domain.api.domain.WorkerTelegramStoreHouseDto
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
}
