package by.miaskor.domain.service

import by.miaskor.domain.api.domain.CarDto
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
      StoreHouseDto(
        name = carDto.storeHouseName,
        chatId = carDto.chatId
      )
    }.flatMap(storeHouseService::getByNameAndTelegramChatId)
      .map { storeHouse ->
        Car(
          body = carDto.body,
          transmission = carDto.transmission,
          engineCapacity = carDto.engineCapacity,
          fuelType = carDto.fuelType,
          engineType = carDto.engineType,
          brandId = carDto.brandId,
          year = carDto.year,
          storeHouseId = storeHouse.id
        )
      }.flatMap(carRepository::create)
  }
}
