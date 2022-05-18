package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.CarDto
import by.miaskor.domain.service.CarService
import org.springframework.http.ResponseEntity
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
}
