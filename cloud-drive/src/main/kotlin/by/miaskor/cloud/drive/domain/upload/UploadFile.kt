package by.miaskor.cloud.drive.domain.upload

import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux

data class UploadFile(
  val filePath: String,
  val file: Flux<DataBuffer>,
)
