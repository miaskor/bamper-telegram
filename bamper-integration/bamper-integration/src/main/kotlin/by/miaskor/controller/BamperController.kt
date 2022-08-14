package by.miaskor.controller

import by.miaskor.domain.AuthDto
import by.miaskor.domain.ImportAdvertisementRequest
import by.miaskor.service.AuthorizationService
import by.miaskor.service.ImportService
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
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

  @PostMapping("/dev/import", consumes = ["*/*"])
  fun importDevAdvertisement(@RequestPart file: Mono<FilePart>, @RequestPart sessionCookie: String): Mono<Unit> {
    return file.flatMap {
      DataBufferUtils.join(it.content())
    }.map { it.asInputStream() }
      .flatMap { importService.importAdvertisement(it, sessionCookie) }
  }

  @PostMapping("/import")
  fun importAdvertisement(@RequestBody importAdvertisementRequest: ImportAdvertisementRequest): Mono<Unit> {
    return importService.importAdvertisement(importAdvertisementRequest)
  }
}
