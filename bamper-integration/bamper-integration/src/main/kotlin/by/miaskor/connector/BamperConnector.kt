package by.miaskor.connector

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.domain.AuthDto
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
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
    builder.part("File", file)
    builder.part("action", "upload")


    return webClient.post()
      .uri(bamperSettings.importUri())
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .cookie("PHPSESSID", sessionCookie)
      .bodyValue(builder.build())
      .retrieve()
      .bodyToMono(String::class.java)
      .flatMap { response ->
        webClient.get()
          .uri(bamperSettings.importUri())
          .cookie("PHPSESSID", sessionCookie)
          .attribute("ts", parseUploadNumber(response))
          .retrieve()
          .bodyToMono()
      }
  }

  private companion object {
    private const val AUTH_COOKIE = "PHPSESSID"
    private fun parseUploadNumber(response: String): String {
      return response.substringAfter("\"ts\":").removeSuffix("}")
    }
  }
}
