package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.service.connector.CloudYandexDriveConnector
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URLDecoder

interface ImageDownloader {

  fun download(downloadFile: DownloadFile): Publisher<DataBuffer>
}

class DefaultImageDownloader(
  private val cloudYandexDriveConnector: CloudYandexDriveConnector,
) : ImageDownloader {
  override fun download(downloadFile: DownloadFile): Flux<DataBuffer> {
    return Mono.just(downloadFile.path)
      .flatMap { cloudYandexDriveConnector.getDownloadedUrl(it) }
      .map { URLDecoder.decode(it, Charsets.UTF_8) }
      .flatMapMany(cloudYandexDriveConnector::getImage)
  }
}
