package by.miaskor.domain.service

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.StoreHouseDto
import by.miaskor.domain.repository.CarRepository
import by.miaskor.domain.tables.pojos.Car
import reactor.core.publisher.Mono

class CarService(
  private val carRepository: CarRepository,
  private val storeHouseService: StoreHouseService
) {

  fun create(carDto: CarDto): Mono<Long> {
    return Mono.fromSupplier {
        Car(
          body = carDto.body,
          transmission = carDto.transmission,
          engineCapacity = carDto.engineCapacity,
          fuelType = carDto.fuelType,
          engineType = carDto.engineType,
          brandId = carDto.brandId,
          year = carDto.year,
          storeHouseId = carDto.storeHouseId
        )
      }.flatMap(carRepository::create)
  }

  fun getByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<CarResponse> {
    return carRepository.findByStoreHouseIdAndId(storeHouseId, id)
      .map {
        CarResponse(it.id ?: -1)
      }
  }
}
