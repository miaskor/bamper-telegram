package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.FilePath
import by.miaskor.cloud.drive.domain.upload.UploadFile
import reactor.core.publisher.Mono

interface CloudDriveService {

  fun getDownloadUrl(filePath: FilePath): Mono<String>
  fun downloadFile(filePath: FilePath): Mono<ByteArray>
  fun uploadFile(uploadFile: UploadFile): Mono<FilePath>
}
