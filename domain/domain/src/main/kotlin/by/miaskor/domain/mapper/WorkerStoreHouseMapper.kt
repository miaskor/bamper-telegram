package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.WorkerStoreHouse
import by.miaskor.domain.tables.records.WorkerStoreHouseRecord
import reactor.core.publisher.Mono

object WorkerStoreHouseMapper : EntityMapper<WorkerStoreHouse, WorkerStoreHouseRecord> {
  override fun map(from: WorkerStoreHouse): Mono<WorkerStoreHouseRecord> {
    return Mono.fromSupplier {
      WorkerStoreHouseRecord().apply {
        workerPrivilege = from.workerPrivilege
        storeHouseId = from.storeHouseId
        workerTelegramClientId = from.workerTelegramClientId
      }
    }
  }
}
