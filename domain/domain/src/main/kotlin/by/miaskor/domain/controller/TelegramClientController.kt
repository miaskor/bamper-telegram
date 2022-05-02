package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.service.TelegramClientService
import org.springframework.http.ResponseEntity
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
  fun create(@RequestBody telegramClientRequest: TelegramClientRequest): Mono<ResponseEntity<Unit>> {
    return telegramClientService.create(telegramClientRequest)
      .map { ResponseEntity.ok().build() }
  }
}
