package by.miaskor.service.imprt

import by.miaskor.configuration.settings.ImportSettings
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Mono

class ImportRequestBuilder(
  private val importSettings: ImportSettings,
) {

  fun buildFormData(file: ByteArray): Mono<MultiValueMap<String, HttpEntity<*>>> {
    return Mono.fromSupplier {
      MultipartBodyBuilder().apply {
        part("File", file)
          .filename(importSettings.importFileName())
          .contentType(MediaType.parseMediaType(importSettings.importMediaType()))
        part("action", "upload")
      }.build()
    }
  }
}
