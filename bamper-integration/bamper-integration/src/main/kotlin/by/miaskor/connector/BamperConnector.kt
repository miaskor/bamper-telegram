package by.miaskor.connector

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.domain.AuthDto
import by.miaskor.service.auth.AuthRequestBuilder
import by.miaskor.service.auth.AuthSessionCookieResolver
import by.miaskor.service.imprt.ImportRequestBuilder
import org.apache.logging.log4j.LogManager
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.InputStream

class BamperConnector(
  private val webClient: WebClient,
  private val bamperSettings: BamperSettings,
  private val authRequestBuilder: AuthRequestBuilder,
  private val authSessionCookieResolver: AuthSessionCookieResolver,
  private val importRequestBuilder: ImportRequestBuilder,
) {

  fun auth(authDto: AuthDto): Mono<String> {
    return authRequestBuilder.buildFormData(authDto)
      .flatMap { formData ->
        webClient.post()
          .uri("${bamperSettings.bamperUrl()}/${bamperSettings.authorizationUri()}")
          .body(BodyInserters.fromFormData(formData))
          .exchangeToMono(authSessionCookieResolver::getSessionCookie)
      }
  }

  fun importAdvertisement(file: InputStream, sessionCookie: String): Mono<Unit> {
    return Mono.fromCallable { file.readAllBytes() }
      .flatMap(importRequestBuilder::buildFormData)
      .flatMap { multiPartBody ->
        webClient.post()
          .uri("${bamperSettings.bamperUrl()}/${bamperSettings.importUri()}")
          .cookie(bamperSettings.sessionCookieName(), sessionCookie)
          .body(BodyInserters.fromMultipartData(multiPartBody))
          .exchangeToMono {
            log.info("status ${it.statusCode()}")
            it.bodyToMono(String::class.java)
          }
      }.doOnNext { log.info("Response $it") }
      .flatMap { response -> parseFile(response, sessionCookie) }
      .subscribeOn(Schedulers.boundedElastic())
  }

  private fun parseFile(response: String, sessionCookie: String): Mono<Unit> {
    return Mono.fromSupplier { parseUploadNumber(response) }
      .flatMap { uploadNumber ->
        webClient.get()
          .uri(bamperSettings.bamperUrl()) { uriBuilder ->
            uriBuilder.path(bamperSettings.parseUri())
              .queryParam("ts", uploadNumber)
              .build()
          }
          .cookie(bamperSettings.sessionCookieName(), sessionCookie)
          .exchangeToMono {
            log.info("status ${it.statusCode()}")
            it.bodyToMono(String::class.java)
          }
      }.doOnNext { log.info("Response $it") }
      .then(Mono.empty())
  }

  private fun parseUploadNumber(response: String): String {
    return response.substringAfter("\"ts\":").removeSuffix("}")
  }

  private companion object {
    private val log = LogManager.getLogger()
  }
}
