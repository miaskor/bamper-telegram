package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.EmployeeExistsRequest
import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.service.TelegramClientService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/telegram-client")
class TelegramClientController(
  private val telegramClientService: TelegramClientService
) {

  @PostMapping
  fun upsert(@RequestBody telegramClientRequest: TelegramClientRequest): Mono<ResponseEntity<Unit>> {
    return telegramClientService.upsert(telegramClientRequest)
      .then(Mono.just(ResponseEntity.ok().build()))
  }

  @GetMapping("/chatId/{chatId}")
  fun getByChatId(@PathVariable chatId: Long): Mono<ResponseEntity<TelegramClientResponse>> {
    return telegramClientService.getByChatId(chatId)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/username/{username}")
  fun getByUsername(@PathVariable username: String): Mono<ResponseEntity<TelegramClientResponse>> {
    return telegramClientService.getByUsername(username)
      .map { ResponseEntity.ok(it) }
  }

  @PostMapping("/isWorker")
  fun isTelegramClientWorker(@RequestBody employeeExistsRequest: EmployeeExistsRequest):
      Mono<ResponseEntity<Boolean>> {
    return telegramClientService.isTelegramClientWorker(employeeExistsRequest)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/employees/{employerChatId}")
  fun getAllEmployees(@PathVariable employerChatId: Long): Mono<ResponseEntity<List<TelegramClientResponse>>> {
    return telegramClientService.getAllEmployees(employerChatId)
      .map { ResponseEntity.ok(it) }
  }
}
