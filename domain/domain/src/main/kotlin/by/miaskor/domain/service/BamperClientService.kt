package by.miaskor.domain.service

import by.miaskor.domain.api.domain.BamperClientDto
import by.miaskor.domain.repository.BamperClientRepository
import by.miaskor.domain.tables.pojos.BamperClient
import reactor.core.publisher.Mono

class BamperClientService(
  private val bamperClientRepository: BamperClientRepository,
) {

  fun save(bamperClientDto: BamperClientDto): Mono<Unit> {
    return Mono.fromSupplier {
      BamperClient(
        login = bamperClientDto.login,
        password = bamperClientDto.password
      )
    }.flatMap(bamperClientRepository::save)
  }
}
