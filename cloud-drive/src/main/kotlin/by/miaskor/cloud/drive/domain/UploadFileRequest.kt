package by.miaskor.cloud.drive.domain

import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux

data class UploadFileRequest(
  val chatId: Long,
  val image: Flux<DataBuffer>,
  val uniqueKey: String
)
