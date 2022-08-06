package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.service.connector.CloudYandexDriveConnector
import reactor.core.publisher.Mono

class CloudYandexDriveService(
  private val cloudYandexDriveConnector: CloudYandexDriveConnector,
) {

  fun getDownloadUrl(downloadFile: DownloadFile): Mono<String> {
    return Mono.just(downloadFile.path)
      .flatMap { cloudYandexDriveConnector.getDownloadedUrl(it) }
  }
}
