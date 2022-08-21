package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.WorkerTelegramDto
import by.miaskor.domain.service.WorkerTelegramService
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
@RequestMapping("/worker-telegrams")
class WorkerTelegramController(
  private val workerTelegramService: WorkerTelegramService,
) {

  @PostMapping
  fun create(@RequestBody workerTelegramDto: WorkerTelegramDto): Mono<ResponseEntity<Unit>> {
    return workerTelegramService.save(workerTelegramDto)
      .then(Mono.just(ResponseEntity.ok().build()))
  }

  @GetMapping(params = ["employeeChatId", "employerChatId"])
  fun find(
    @RequestParam employerChatId: Long,
    @RequestParam employeeChatId: Long,
  ): Mono<ResponseEntity<WorkerTelegramDto>> {
    return Mono.fromSupplier {
      WorkerTelegramDto(
        employeeChatId = employeeChatId,
        employerChatId = employerChatId
      )
    }.flatMap(workerTelegramService::get)
      .map { ResponseEntity.ok(it) }
  }

  @DeleteMapping(params = ["employeeChatId", "employerChatId"])
  fun remove(
    @RequestParam employerChatId: Long,
    @RequestParam employeeChatId: Long,
  ): Mono<ResponseEntity<Unit>> {
    return Mono.fromSupplier {
      WorkerTelegramDto(
        employeeChatId = employeeChatId,
        employerChatId = employerChatId
      )
    }.flatMap(workerTelegramService::remove)
      .then(Mono.just(ResponseEntity.ok().build()))
  }
}
