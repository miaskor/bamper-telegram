package by.miaskor.connector

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.domain.AuthDto
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.InputStream

class BamperConnector(
  private val webClient: WebClient,
  private val bamperSettings: BamperSettings,
) {

  fun auth(authDto: AuthDto): Mono<String> {
    val authorizationFormData = bamperSettings.authorizationFormData()
    val mapOf = mapOf(
      Pair("AUTH_FORM", listOf(authorizationFormData[0])),
      Pair("TYPE", listOf(authorizationFormData[1])),
      Pair("backurl", listOf(authorizationFormData[2])),
      Pair("Login", listOf(authorizationFormData[3])),
      Pair("USER_LOGIN", listOf(authDto.login)),
      Pair("USER_PASSWORD", listOf(authDto.password))
    )
    val multiValueMap = LinkedMultiValueMap(mapOf)

    return webClient.post()
      .uri("${bamperSettings.bamperUrl()}/${bamperSettings.authorizationUri()}")
      .body(BodyInserters.fromFormData(multiValueMap))
      .exchangeToMono { response ->
        val isSuccessful = response.headers().header("Set-Cookie").any {
          it.contains("BITRIX_SM_LOGIN")
        }
        if (isSuccessful) {
          Mono.fromSupplier {
            response.cookies()[AUTH_COOKIE]?.first()?.value ?: ""
          }
        } else {
          Mono.empty()
        }
      }
  }

  fun importAdvertisement(file: InputStream, sessionCookie: String): Mono<Unit> {
    return Mono.fromCallable { file.readAllBytes() }
      .map(::createMultiPartBody)
      .flatMap { multiPartBody ->
        webClient.post()
          .uri("${bamperSettings.bamperUrl()}/${bamperSettings.importUri()}")
          .cookie(AUTH_COOKIE, sessionCookie)
          .body(BodyInserters.fromMultipartData(multiPartBody))
          .exchangeToMono {
            log.info("status ${it.statusCode()}")
            it.bodyToMono(String::class.java)
          }
      }.doOnNext { log.info("Response $it") }
      .flatMap { response -> parseFile(response, sessionCookie) }
      .subscribeOn(Schedulers.boundedElastic())
  }

  private fun createMultiPartBody(file: ByteArray): MultiValueMap<String, HttpEntity<*>> {
    return MultipartBodyBuilder().apply {
      part("File", file)
        .filename("file_to_upload.xls")
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
      part("action", "upload")
    }.build()
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
          .cookie(AUTH_COOKIE, sessionCookie)
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
    private const val AUTH_COOKIE = "PHPSESSID"
    private val log = LogManager.getLogger()
  }
}
