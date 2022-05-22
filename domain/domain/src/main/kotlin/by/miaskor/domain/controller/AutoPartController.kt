package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.service.AutoPartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController("/auto-part")
class AutoPartController(
  private val autoPartService: AutoPartService
) {

  @PostMapping
  fun create(@RequestBody autoPartDto: AutoPartDto): Mono<ResponseEntity<Unit>> {
    return autoPartService.create(autoPartDto)
      .then(Mono.just(ResponseEntity.ok().build()))
  }
}
