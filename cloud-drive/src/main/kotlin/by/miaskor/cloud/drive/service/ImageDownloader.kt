package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.service.connector.CloudYandexDriveConnector
import org.springframework.core.io.buffer.DataBufferUtils
import reactor.core.publisher.Mono
import java.net.URLDecoder

interface ImageDownloader {

  fun download(downloadFile: DownloadFile): Mono<ByteArray>
}

class DefaultImageDownloader(
  private val cloudYandexDriveConnector: CloudYandexDriveConnector,
) : ImageDownloader {
  override fun download(downloadFile: DownloadFile): Mono<ByteArray> {
    return cloudYandexDriveConnector.getDownloadedUrl(downloadFile.path)
      .map { downloadUrl -> URLDecoder.decode(downloadUrl, Charsets.UTF_8) }
      .flatMap { downloadUrl ->
        DataBufferUtils.join(cloudYandexDriveConnector.getImage(downloadUrl))
      }
      .map { dataBuffer -> dataBuffer.asByteBuffer().array() }
  }
}
