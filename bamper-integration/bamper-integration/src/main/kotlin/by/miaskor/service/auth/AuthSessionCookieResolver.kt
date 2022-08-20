package by.miaskor.service.auth

import by.miaskor.configuration.settings.BamperSettings
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

class AuthSessionCookieResolver(
  private val bamperSettings: BamperSettings,
) {

  fun getSessionCookie(clientResponse: ClientResponse): Mono<String> {
    return Mono.justOrEmpty(
      clientResponse.cookies()[bamperSettings.sessionCookieName()]
        ?.first()
        ?.value
    )
  }
}
