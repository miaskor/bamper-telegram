package by.miaskor.service.auth

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.domain.AuthDto
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Mono

class AuthRequestBuilder(
  private val bamperSettings: BamperSettings,
) {

  fun buildFormData(authDto: AuthDto): Mono<MultiValueMap<String, String>> {
    val authorizationFormData = bamperSettings.authorizationFormData()
    val mapOf = mapOf(
      Pair("AUTH_FORM", listOf(authorizationFormData[0])),
      Pair("TYPE", listOf(authorizationFormData[1])),
      Pair("backurl", listOf(authorizationFormData[2])),
      Pair("Login", listOf(authorizationFormData[3])),
      Pair("USER_LOGIN", listOf(authDto.login)),
      Pair("USER_PASSWORD", listOf(authDto.password))
    )
    return Mono.just(LinkedMultiValueMap(mapOf))
  }
}
