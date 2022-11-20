package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.FilePath
import reactor.core.publisher.Mono

interface FileDownloader {

  fun download(filePath: FilePath): Mono<ByteArray>
}
