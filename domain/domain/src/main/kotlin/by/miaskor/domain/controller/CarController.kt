package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.service.CarService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/car")
class CarController(
  private val carService: CarService
) {

  @PostMapping
  fun create(@RequestBody carDto: CarDto): Mono<ResponseEntity<Long>> {
    return carService.create(carDto)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/{storeHouseId}/{id}")
  fun getById(@PathVariable storeHouseId: Long, @PathVariable id: Long): Mono<ResponseEntity<CarResponse>> {
    return carService.getByStoreHouseIdAndId(storeHouseId, id)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/{storeHouseId}")
  fun getById(@PathVariable storeHouseId: Long): Mono<ResponseEntity<List<CarResponse>>> {
    return carService.getByAllStoreHouseId(storeHouseId)
      .map { ResponseEntity.ok(it) }
  }
}
