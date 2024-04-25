package by.miaskor.service.imprt

import by.miaskor.connector.BamperConnector
import by.miaskor.domain.ImportAdvertisementRequest
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.service.excel.ExcelGenerator
import reactor.core.publisher.Mono

class ImportService(
  private val bamperConnector: BamperConnector,
  private val autoPartConnector: AutoPartConnector,
  private val excelGenerator: ExcelGenerator,
  private val autoPartResponseMapper: AutoPartResponseMapper,
) {
  fun importAdvertisement(importAdvertisementRequest: ImportAdvertisementRequest): Mono<Unit> {
    return autoPartConnector.getByTelegramChatIdAndId(
      importAdvertisementRequest.telegramCharId,
      importAdvertisementRequest.autoPartId
    )
      .map(autoPartResponseMapper::map)
      .flatMap(excelGenerator::generate)
      .flatMap { file -> bamperConnector.importAdvertisement(file, importAdvertisementRequest.bamperSessionId) }
  }
}
