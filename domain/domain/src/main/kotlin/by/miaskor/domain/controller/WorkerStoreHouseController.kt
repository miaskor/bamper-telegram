package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.WorkerTelegramStoreHouseDto
import by.miaskor.domain.service.WorkerStoreHouseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/worker-store-houses")
class WorkerStoreHouseController(
  private val workerStoreHouseService: WorkerStoreHouseService
) {

  @PostMapping
  fun create(@RequestBody workerTelegramStoreHouseDto: WorkerTelegramStoreHouseDto): Mono<ResponseEntity<Unit>> {
    return workerStoreHouseService.create(workerTelegramStoreHouseDto)
      .map { ResponseEntity.ok().build() }
  }
}
