package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.service.connector.CloudYandexDriveConnector
import reactor.core.publisher.Mono

interface ImageDownloader {

  fun download(downloadFile: DownloadFile): Mono<ByteArray>
}

class DefaultImageDownloader(
  private val cloudYandexDriveConnector: CloudYandexDriveConnector
) : ImageDownloader {
  override fun download(downloadFile: DownloadFile): Mono<ByteArray> {
    return Mono.just(downloadFile.downloadUrl)
      .flatMap(cloudYandexDriveConnector::getImage)
  }
}
