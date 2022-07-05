package by.miaskor.service

import by.miaskor.connector.BamperConnector
import by.miaskor.domain.AuthDto
import reactor.core.publisher.Mono

class AuthorizationService(
  private val bamperConnector: BamperConnector,
) {

  fun auth(authDto: AuthDto): Mono<String> {
    return bamperConnector.auth(authDto)
  }
}
