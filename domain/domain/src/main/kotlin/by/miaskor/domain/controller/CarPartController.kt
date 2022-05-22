package by.miaskor.domain.controller

import by.miaskor.domain.service.CarPartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/car-part")
class CarPartController(
  private val carPartService: CarPartService
) {

  @GetMapping("/{name}")
  fun getIdByName(@PathVariable name: String): Mono<ResponseEntity<Long>> {
    return carPartService.getIdByName(name)
      .map { ResponseEntity.ok(it) }
  }
}
