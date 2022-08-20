package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdWithLimitRequest
import by.miaskor.domain.service.CarService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/cars")
class CarController(
  private val carService: CarService,
) {

  @PostMapping
  fun create(@RequestBody carDto: CarDto): Mono<ResponseEntity<Long>> {
    return carService.create(carDto)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping(params = ["storeHouseId", "id"])
  fun getById(@RequestParam storeHouseId: Long, @RequestParam id: Long): Mono<ResponseEntity<CarResponse>> {
    return carService.getByStoreHouseIdAndId(storeHouseId, id)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping(params = ["storeHouseId", "limit", "offset"])
  fun getByStoreHouseId(
    @RequestParam storeHouseId: Long,
    @RequestParam limit: Long,
    @RequestParam offset: Long,
  ): Mono<ResponseEntity<ResponseWithLimit<CarResponse>>> {
    return Mono.fromSupplier {
      StoreHouseIdWithLimitRequest(
        storeHouseId = storeHouseId,
        limit = limit,
        offset = offset
      )
    }.flatMap(carService::getAllByStoreHouseId)
      .map { ResponseEntity.ok(it) }
  }

  @DeleteMapping(params = ["storeHouseId", "id"])
  fun deleteById(@RequestParam storeHouseId: Long, @RequestParam id: Long): Mono<ResponseEntity<Boolean>> {
    return carService.deleteByStoreHouseIdAndId(storeHouseId, id)
      .map { ResponseEntity.ok(it) }
  }
}
