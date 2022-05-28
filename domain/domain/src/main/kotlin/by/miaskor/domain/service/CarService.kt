package by.miaskor.domain.service

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
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

  fun getAllByStoreHouseId(storeHouseIdRequest: StoreHouseIdRequest): Mono<ResponseWithLimit<CarResponse>> {
    return carRepository.findAllByStoreHouseId(
      storeHouseIdRequest.storeHouseId,
      storeHouseIdRequest.limit + 1,
      storeHouseIdRequest.offset
    )
      .map {
        val isMoreExists = it.size > 10
        val cars = if (isMoreExists) it.dropLast(1) else it
        ResponseWithLimit(
          entities = cars,
          isMoreExists = isMoreExists
        )
      }
  }
}
