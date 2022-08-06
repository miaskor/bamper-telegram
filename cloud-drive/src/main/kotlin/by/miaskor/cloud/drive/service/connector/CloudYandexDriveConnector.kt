package by.miaskor.cloud.drive.service.connector

import by.miaskor.cloud.drive.domain.DownloadUrlResponse
import by.miaskor.cloud.drive.domain.UploadUrlResponse
import by.miaskor.cloud.drive.settings.CloudDriveSettings
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CloudYandexDriveConnector(
  private val webClient: WebClient,
  private val cloudDriveSettings: CloudDriveSettings,
) {

  fun uploadImage(image: Flux<DataBuffer>, path: String): Mono<Unit> {
    return getUploadedUrl(path)
      .flatMap {
        webClient.put()
          .uri(it)
          .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
          .body(BodyInserters.fromPublisher(image, DataBuffer::class.java))
          .retrieve()
          .bodyToMono(Unit::class.java)
      }
  }

  fun createFolder(nameFolder: String): Mono<Unit> {
    return webClient.put()
      .uri { uriBuilder ->
        uriBuilder.path(cloudDriveSettings.createFolderUri())
          .queryParam("path", nameFolder)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .retrieve()
      .bodyToMono(Unit::class.java)
  }

  fun getImage(downloadUrl: String): Flux<DataBuffer> {
    return webClient.get()
      .uri(downloadUrl)
      .accept(MediaType.APPLICATION_OCTET_STREAM)
      .retrieve()
      .bodyToFlux(DataBuffer::class.java)
  }

  fun isFolderExists(path: String): Mono<Boolean> {
    return webClient.get()
      .uri { uriBuilder ->
        uriBuilder.path(cloudDriveSettings.folderInformationUri())
          .queryParam("path", path)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .exchangeToMono {
        Mono.just(it.statusCode())
          .filter { it.is2xxSuccessful }
          .map { true }
          .defaultIfEmpty(false)
      }
  }

  fun getDownloadedUrl(path: String): Mono<String> {
    return webClient.get()
      .uri { uriBuilder ->
        uriBuilder.path(cloudDriveSettings.getDownloadedUri())
          .queryParam("path", path)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .retrieve()
      .bodyToMono(DownloadUrlResponse::class.java)
      .map { it.href }
  }

  private fun getUploadedUrl(path: String): Mono<String> {
    return webClient.get()
      .uri { uriBuilder ->
        uriBuilder.path(cloudDriveSettings.getUploadedUri())
          .queryParam("path", path)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .retrieve()
      .bodyToMono(UploadUrlResponse::class.java)
      .map { it.href }
  }

  private companion object {
    private const val AUTHORIZATION_HEADER = "Authorization"
  }
}
