package by.miaskor.domain.service.telegram

import org.springframework.core.io.buffer.DataBuffer
import reactor.core.publisher.Flux

class TelegramService(
  private val telegramConnector: TelegramConnector,
) {

  fun getPhoto(photoId: String): Flux<DataBuffer> {
    return telegramConnector.getPhotoPath(photoId)
      .flatMapMany(telegramConnector::getPhoto)
  }
}
