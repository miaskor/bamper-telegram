package by.miaskor.domain.controller

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
      .map { ResponseEntity.ok().build() }
  }

  @GetMapping("/{chatId}")
  fun getByChatId(@PathVariable chatId: Long): Mono<ResponseEntity<TelegramClientResponse>> {
    return telegramClientService.getByChatId(chatId)
      .map { ResponseEntity.ok(it) }
  }
}
