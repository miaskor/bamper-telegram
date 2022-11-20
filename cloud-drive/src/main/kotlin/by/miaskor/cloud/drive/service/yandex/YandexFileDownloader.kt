package by.miaskor.cloud.drive.service.yandex

import by.miaskor.cloud.drive.connector.yandex.YandexCloudDriveConnector
import by.miaskor.cloud.drive.domain.FilePath
import by.miaskor.cloud.drive.service.FileDownloader
import org.springframework.core.io.buffer.DataBufferUtils
import reactor.core.publisher.Mono
import java.net.URLDecoder

class YandexFileDownloader(private val yandexCloudDriveConnector: YandexCloudDriveConnector) : FileDownloader {
  override fun download(filePath: FilePath): Mono<ByteArray> {
    return yandexCloudDriveConnector.getDownloadedUrl(filePath.value)
      .map { downloadUrl -> URLDecoder.decode(downloadUrl, Charsets.UTF_8) }
      .map(yandexCloudDriveConnector::getFile)
      .flatMap(DataBufferUtils::join)
      .map { dataBuffer -> dataBuffer.asByteBuffer().array() }
  }
}
