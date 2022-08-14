package by.miaskor.service

import by.miaskor.connector.BamperConnector
import by.miaskor.domain.ImportAdvertisementRequest
import by.miaskor.domain.api.connector.AutoPartConnector
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.InputStream

class ImportService(
  private val bamperConnector: BamperConnector,
  private val autoPartConnector: AutoPartConnector,
  private val excelGenerator: ExcelGenerator,
) {

  fun importAdvertisement(file: InputStream, sessionCookie: String): Mono<Unit> {
    return bamperConnector.importAdvertisement(file, sessionCookie)
  }

  fun importAdvertisement(importAdvertisementRequest: ImportAdvertisementRequest): Mono<Unit> {
    return autoPartConnector.getByTelegramChatIdAndId(
      importAdvertisementRequest.telegramCharId,
      importAdvertisementRequest.autoPartId
    )
      .flatMap(excelGenerator::generate)
      .flatMap {
        bamperConnector.importAdvertisement(it, importAdvertisementRequest.bamperSessionId)
      }
      .subscribeOn(Schedulers.boundedElastic())
  }
}
