package by.miaskor.cloud.drive.connector.yandex

import by.miaskor.cloud.drive.configuration.settings.CloudDriveSettings
import by.miaskor.cloud.drive.domain.download.DownloadUrlResponse
import by.miaskor.cloud.drive.domain.upload.UploadUrlResponse
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class YandexCloudDriveConnector(
  private val webClient: WebClient,
  private val cloudDriveSettings: CloudDriveSettings,
) {

  fun uploadImage(image: Flux<DataBuffer>, path: String): Mono<Unit> {
    return getUploadedUrl(path)
      .flatMap { url ->
        webClient.put()
          .uri(url)
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
          .queryParam(PATH_PARAM, nameFolder)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .retrieve()
      .bodyToMono()
  }

  fun getFile(downloadUrl: String): Flux<DataBuffer> {
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
          .queryParam(PATH_PARAM, path)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .exchangeToMono { clientResponse ->
        Mono.fromSupplier { clientResponse.statusCode().is2xxSuccessful }
          .defaultIfEmpty(false)
      }
  }

  fun getDownloadedUrl(path: String): Mono<String> {
    return webClient.get()
      .uri { uriBuilder ->
        uriBuilder.path(cloudDriveSettings.getDownloadedUri())
          .queryParam(PATH_PARAM, path)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .retrieve()
      .bodyToMono(DownloadUrlResponse::class.java)
      .map(DownloadUrlResponse::href)
  }

  private fun getUploadedUrl(path: String): Mono<String> {
    return webClient.get()
      .uri { uriBuilder ->
        uriBuilder.path(cloudDriveSettings.getUploadedUri())
          .queryParam(PATH_PARAM, path)
          .build()
      }
      .header(AUTHORIZATION_HEADER, cloudDriveSettings.authToken())
      .retrieve()
      .bodyToMono(UploadUrlResponse::class.java)
      .map(UploadUrlResponse::href)
  }

  private companion object {
    const val AUTHORIZATION_HEADER = "Authorization"
    const val PATH_PARAM = "path"
  }
}
