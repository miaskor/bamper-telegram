package by.miaskor.domain.service

import by.miaskor.domain.repository.CarPartRepository
import reactor.core.publisher.Mono

class CarPartService(
  private val carPartRepository: CarPartRepository
) {

  fun getIdByName(carPartName: String): Mono<Long> {
    return carPartRepository.findIdByName(carPartName)
      .mapNotNull { it.id }
  }
}
