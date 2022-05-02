package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.WorkerTelegram
import by.miaskor.domain.tables.records.WorkerTelegramRecord
import reactor.core.publisher.Mono

object WorkerTelegramMapper : EntityMapper<WorkerTelegram, WorkerTelegramRecord> {
  override fun map(from: WorkerTelegram): Mono<WorkerTelegramRecord> {
    return Mono.fromSupplier {
      WorkerTelegramRecord().apply {
        workerTelegramClientId = from.workerTelegramClientId
        employerTelegramClientId = from.employerTelegramClientId
      }
    }
  }
}
