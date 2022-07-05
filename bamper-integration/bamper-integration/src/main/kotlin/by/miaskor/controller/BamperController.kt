package by.miaskor.controller

import by.miaskor.domain.AuthDto
import by.miaskor.service.AuthorizationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/bamper")
class BamperController(
  private val authorizationService: AuthorizationService,
) {

  @PostMapping("/auth")
  fun auth(@RequestBody authDto: AuthDto): Mono<String> {
    return authorizationService.auth(authDto)
  }
}
