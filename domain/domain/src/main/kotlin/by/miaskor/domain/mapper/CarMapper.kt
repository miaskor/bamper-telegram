package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.Car
import by.miaskor.domain.tables.records.CarRecord
import reactor.core.publisher.Mono

object CarMapper : EntityMapper<Car, CarRecord> {
  override fun map(from: Car): Mono<CarRecord> {
    return Mono.fromSupplier {
      CarRecord().apply {
        id = null
        body = from.body
        transmission = from.transmission
        engineCapacity = from.engineCapacity
        fuelType = from.fuelType
        engineType = from.engineType
        brandId = from.brandId
      }
    }
  }
}
