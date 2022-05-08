package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.WorkerTelegramRequest
import by.miaskor.domain.service.WorkerTelegramService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/worker-telegram")
class WorkerTelegramController(
  private val workerTelegramService: WorkerTelegramService
) {

  @PostMapping
  fun create(@RequestBody workerTelegramRequest: WorkerTelegramRequest): Mono<ResponseEntity<Unit>> {
    return workerTelegramService.save(workerTelegramRequest)
      .map { ResponseEntity.ok().build() }
  }
}
