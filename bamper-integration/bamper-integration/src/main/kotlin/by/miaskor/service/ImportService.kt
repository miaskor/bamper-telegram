package by.miaskor.service

import by.miaskor.connector.BamperConnector
import reactor.core.publisher.Mono
import java.io.InputStream

class ImportService(
  private val bamperConnector: BamperConnector,
) {

  fun importAdvertisement(file: InputStream, sessionCookie: String): Mono<Unit> {
    return bamperConnector.importAdvertisement(file, sessionCookie)
  }
}
