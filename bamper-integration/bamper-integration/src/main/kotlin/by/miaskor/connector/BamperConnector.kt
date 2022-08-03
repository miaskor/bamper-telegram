package by.miaskor.connector

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.domain.AuthDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
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
      .uri(bamperSettings.authorizationUri())
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

    val builder = MultipartBodyBuilder()
    val header1 = "form-data; name=attach; filename=file_to_upload_$sessionCookie.xlsx"
    builder.part("File", file.readAllBytes(), MediaType.APPLICATION_OCTET_STREAM).header(
      "Content-Disposition",
      header1
    )
    builder.part("action", "upload")


    return Mono.fromSupplier {
      val headers = HttpHeaders()
      headers.add("Cookie", "$AUTH_COOKIE=$sessionCookie")
      val requestEntity: HttpEntity<MultiValueMap<String, *>> = HttpEntity(builder.build(), headers)

      val serverUrl = "https://bamper.by/personal/import/handler.php"

      val restTemplate = RestTemplate()
      restTemplate
        .postForEntity(serverUrl, requestEntity, String::class.java)
    }.flatMap { parseFile(it.body!!, sessionCookie) }
  }

  private fun parseFile(response: String, sessionCookie: String): Mono<Unit> {
    return Mono.fromSupplier { parseUploadNumber(response) }
      .flatMap { uploadNumber ->
        webClient.get()
          .uri(bamperSettings.parseUri())
          .cookie(AUTH_COOKIE, sessionCookie)
          .attribute("ts", uploadNumber)
          .retrieve()
          .toBodilessEntity()
          .then(Mono.empty())
      }
  }

  private companion object {
    private const val AUTH_COOKIE = "PHPSESSID"
    private fun parseUploadNumber(response: String): String {
      return response.substringAfter("\"ts\":").removeSuffix("}")
    }
  }
}
