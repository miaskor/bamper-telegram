package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.FilePath
import by.miaskor.cloud.drive.domain.upload.UploadFile
import reactor.core.publisher.Mono

interface FileUploader {
  fun upload(uploadFile: UploadFile): Mono<FilePath>
}
