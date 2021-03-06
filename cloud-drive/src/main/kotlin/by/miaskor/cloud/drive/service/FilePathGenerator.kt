package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.UploadFileRequest
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface FilePathGenerator {

  fun generate(uploadFileRequest: UploadFileRequest): Mono<String>
}

class DefaultFilePathGenerator : FilePathGenerator {
  override fun generate(uploadFileRequest: UploadFileRequest): Mono<String> {
    return Mono.fromSupplier {
      "${uploadFileRequest.chatId}/${uploadFileRequest.uniqueKey}-${getTime()}"
    }
  }

  private fun getTime(): String {
    val ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH-mm-ss")
    return LocalDateTime.now().format(ofPattern)
  }
}
