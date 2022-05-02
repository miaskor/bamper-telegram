package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.CarPart
import by.miaskor.domain.tables.records.CarPartRecord
import reactor.core.publisher.Mono

object CarPartMapper: EntityMapper<CarPart, CarPartRecord> {
  override fun map(from: CarPart): Mono<CarPartRecord> {
    return Mono.fromSupplier {
      CarPartRecord().apply {
        id = null
        nameEn = from.nameEn
        nameRu = from.nameRu
      }
    }
  }
}
