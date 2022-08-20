package by.miaskor.domain.controller

import by.miaskor.domain.service.CarPartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/car-parts")
class CarPartController(
  private val carPartService: CarPartService,
) {

  @GetMapping(params = ["name"])
  fun getIdByName(@RequestParam name: String): Mono<ResponseEntity<Long>> {
    return carPartService.getIdByName(name)
      .map { ResponseEntity.ok(it) }
  }
}
