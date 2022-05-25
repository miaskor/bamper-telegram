package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.service.AutoPartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auto-part")
class AutoPartController(
  private val autoPartService: AutoPartService
) {

  @PostMapping
  fun create(@RequestBody autoPartDto: AutoPartDto): Mono<ResponseEntity<Unit>> {
    return autoPartService.create(autoPartDto)
      .then(Mono.just(ResponseEntity.ok().build()))
  }

  @GetMapping("/{storeHouseId}")
  fun getByStoreHouseId(@PathVariable storeHouseId: Long): Mono<ResponseEntity<List<AutoPartResponse>>> {
    return autoPartService.getAllByStoreHouseId(storeHouseId)
      .map { ResponseEntity.ok(it) }
  }
}
