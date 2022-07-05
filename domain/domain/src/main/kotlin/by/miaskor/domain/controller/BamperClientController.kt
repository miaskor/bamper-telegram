package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.BamperClientDto
import by.miaskor.domain.service.BamperClientService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/bamper-client")
class BamperClientController(
  private val bamperClientService: BamperClientService,
) {

  @PostMapping("/create")
  fun create(@RequestBody bamperClientDto: BamperClientDto): Mono<Unit> {
    return bamperClientService.save(bamperClientDto)
  }
}
