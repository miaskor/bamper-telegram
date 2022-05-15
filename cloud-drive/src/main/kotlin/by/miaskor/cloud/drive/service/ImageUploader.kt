package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.UploadFileRequest
import by.miaskor.cloud.drive.domain.UploadFileResponse
import by.miaskor.cloud.drive.service.connector.CloudYandexDriveConnector
import reactor.core.publisher.Mono

interface ImageUploader {
  fun upload(uploadFileRequest: UploadFileRequest): Mono<UploadFileResponse>
}

class DefaultImageUploader(
  private val cloudYandexDriveConnector: CloudYandexDriveConnector,
  private val folderCreator: FolderCreator,
  private val filePathGenerator: FilePathGenerator
) : ImageUploader {
  override fun upload(uploadFileRequest: UploadFileRequest): Mono<UploadFileResponse> {
    return Mono.just(uploadFileRequest.chatId)
      .filterWhen { cloudYandexDriveConnector.isFolderExists(it.toString()) }
      .switchIfEmpty(
        folderCreator.create(uploadFileRequest.chatId.toString())
          .thenReturn(uploadFileRequest.chatId)
      )
      .flatMap { filePathGenerator.generate(uploadFileRequest) }
      .flatMap { upload(it, uploadFileRequest.image) }
  }

  private fun upload(path: String, image: ByteArray): Mono<UploadFileResponse> {
    return Mono.just(path)
      .flatMap { cloudYandexDriveConnector.uploadImage(image, it) }
      .flatMap { cloudYandexDriveConnector.getDownloadedUrl(path) }
      .map {
        UploadFileResponse(
          path = path,
          downloadUrl = it
        )
      }
  }
}
