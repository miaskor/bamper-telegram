package by.miaskor.cloud.drive.service.yandex

import by.miaskor.cloud.drive.connector.yandex.YandexCloudDriveConnector
import by.miaskor.cloud.drive.domain.FilePath
import by.miaskor.cloud.drive.domain.upload.UploadFile
import by.miaskor.cloud.drive.service.FileUploader
import by.miaskor.cloud.drive.service.FolderService
import reactor.core.publisher.Mono

class YandexFileUploader(
  private val yandexCloudDriveConnector: YandexCloudDriveConnector,
  private val folderService: FolderService,
) : FileUploader {

  override fun upload(uploadFile: UploadFile): Mono<FilePath> {
    return Mono.fromSupplier { uploadFile.filePath.substringBeforeLast(FILE_DELIMITER) }
      .flatMap { folderPath -> folderService.createIfRequire(folderPath) }
      .then(yandexCloudDriveConnector.uploadImage(uploadFile.file, uploadFile.filePath))
      .thenReturn(FilePath(uploadFile.filePath))
  }

  private companion object {
    const val FILE_DELIMITER = "/"
  }
}
