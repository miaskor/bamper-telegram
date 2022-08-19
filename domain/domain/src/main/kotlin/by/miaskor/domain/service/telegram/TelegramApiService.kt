package by.miaskor.domain.service.telegram

import by.miaskor.domain.service.telegram.domain.PhotoPathResponse
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class TelegramApiService(
  private val webClient: WebClient,
  private val getPhotoPathUri: String,
  private val getPhotoUri: String,
) {

  fun getPhotoPath(photoId: String): Mono<String> {
    return webClient
      .get()
      .uri { uriBuilder ->
        uriBuilder.replacePath(getPhotoPathUri)
          .queryParam("file_id", photoId)
          .build()
      }
      .retrieve()
      .bodyToMono(PhotoPathResponse::class.java)
      .map { response -> response.result.file_path }
  }

  fun getPhoto(photoPath: String): Flux<DataBuffer> {
    return webClient
      .get()
      .uri("${getPhotoUri}/$photoPath")
      .accept(MediaType.APPLICATION_OCTET_STREAM)
      .retrieve()
      .bodyToFlux(DataBuffer::class.java)
  }
}
