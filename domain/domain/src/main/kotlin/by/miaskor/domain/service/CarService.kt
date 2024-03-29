package by.miaskor.domain.service

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdWithLimitRequest
import by.miaskor.domain.repository.CarRepository
import by.miaskor.domain.service.mapper.CarMapper
import by.miaskor.domain.tables.pojos.Car
import reactor.core.publisher.Mono

class CarService(
  private val carRepository: CarRepository,
  private val carMapper: CarMapper,
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
      .map { car ->
        CarResponse(car.id ?: -1)
      }
  }

  fun getAllByStoreHouseId(storeHouseIdWithLimitRequest: StoreHouseIdWithLimitRequest): Mono<ResponseWithLimit<CarResponse>> {
    return carRepository.findAllByStoreHouseId(
      storeHouseIdWithLimitRequest.storeHouseId,
      storeHouseIdWithLimitRequest.limit + 1,
      storeHouseIdWithLimitRequest.offset
    ).map(carMapper::map)
      .map {
        val isMoreExists = it.size > 10
        val cars = if (isMoreExists) it.dropLast(1) else it
        ResponseWithLimit(
          entities = cars,
          isMoreExists = isMoreExists
        )
      }
  }

  fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Boolean> {
    return carRepository.deleteByStoreHouseIdAndId(storeHouseId, id)
      .flatMap(::isDeletionSuccessful)
      .onErrorReturn(false)
  }

  private fun isDeletionSuccessful(deletedRecords: Int): Mono<Boolean> {
    return if (deletedRecords > 0)
      Mono.just(true)
    else
      Mono.empty()
  }
}
