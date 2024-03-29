package by.miaskor.controller

import by.miaskor.domain.AuthDto
import by.miaskor.domain.ImportAdvertisementRequest
import by.miaskor.service.auth.AuthorizationService
import by.miaskor.service.imprt.ImportService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/bamper")
class BamperController(
  private val authorizationService: AuthorizationService,
  private val importService: ImportService,
) {

  @PostMapping("/auth")
  fun auth(@RequestBody authDto: AuthDto): Mono<String> {
    return authorizationService.auth(authDto)
  }

  @PostMapping("/import")
  fun importAdvertisement(@RequestBody importAdvertisementRequest: ImportAdvertisementRequest): Mono<Unit> {
    return importService.importAdvertisement(importAdvertisementRequest)
  }
}
