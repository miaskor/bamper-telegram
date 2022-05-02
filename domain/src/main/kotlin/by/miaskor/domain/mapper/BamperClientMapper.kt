package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.BamperClient
import by.miaskor.domain.tables.records.BamperClientRecord
import reactor.core.publisher.Mono

object BamperClientMapper : EntityMapper<BamperClient, BamperClientRecord> {
  override fun map(from: BamperClient): Mono<BamperClientRecord> {
    return Mono.fromSupplier {
      BamperClientRecord().apply {
        id = null
        login = from.login
        password = from.password
      }
    }
  }
}
