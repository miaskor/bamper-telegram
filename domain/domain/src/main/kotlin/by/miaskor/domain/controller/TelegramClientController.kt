package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.service.TelegramClientService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/telegram-clients")
class TelegramClientController(
  private val telegramClientService: TelegramClientService,
) {

  @PostMapping
  fun upsert(@RequestBody telegramClientRequest: TelegramClientRequest): Mono<ResponseEntity<Unit>> {
    return telegramClientService.upsert(telegramClientRequest)
      .then(Mono.just(ResponseEntity.ok().build()))
  }

  @GetMapping(params = ["chatId"])
  fun getByChatId(@RequestParam chatId: Long): Mono<ResponseEntity<TelegramClientResponse>> {
    return telegramClientService.getByChatId(chatId)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping(params = ["username"])
  fun getByUsername(@RequestParam username: String): Mono<ResponseEntity<TelegramClientResponse>> {
    return telegramClientService.getByUsername(username)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping(params = ["employerChatId"])
  fun getAllEmployees(@RequestParam employerChatId: Long): Mono<ResponseEntity<List<TelegramClientResponse>>> {
    return telegramClientService.getAllEmployees(employerChatId)
      .map { ResponseEntity.ok(it) }
  }
}
