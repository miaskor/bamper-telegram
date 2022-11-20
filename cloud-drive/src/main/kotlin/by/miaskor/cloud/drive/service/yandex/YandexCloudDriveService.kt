package by.miaskor.cloud.drive.service.yandex

import by.miaskor.cloud.drive.connector.yandex.YandexCloudDriveConnector
import by.miaskor.cloud.drive.domain.FilePath
import by.miaskor.cloud.drive.domain.upload.UploadFile
import by.miaskor.cloud.drive.service.CloudDriveService
import by.miaskor.cloud.drive.service.FileDownloader
import by.miaskor.cloud.drive.service.FileUploader
import reactor.core.publisher.Mono

class YandexCloudDriveService(
  private val yandexCloudDriveConnector: YandexCloudDriveConnector,
  private val fileDownloader: FileDownloader,
  private val fileUploader: FileUploader,
) : CloudDriveService {

  override fun getDownloadUrl(filePath: FilePath): Mono<String> {
    return Mono.just(filePath.value)
      .flatMap(yandexCloudDriveConnector::getDownloadedUrl)
  }

  override fun downloadFile(filePath: FilePath): Mono<ByteArray> {
    return fileDownloader.download(filePath)
  }

  override fun uploadFile(uploadFile: UploadFile): Mono<FilePath> {
    return fileUploader.upload(uploadFile)
  }
}
