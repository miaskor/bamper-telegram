package by.miaskor.connector

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.domain.AuthDto
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

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

  private companion object {
    private const val AUTH_COOKIE = "PHPSESSID"
  }
}
