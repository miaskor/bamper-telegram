package by.miaskor.configuration

import by.miaskor.connector.BamperConnector
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.service.auth.AuthRequestBuilder
import by.miaskor.service.auth.AuthSessionCookieResolver
import by.miaskor.service.imprt.ImportRequestBuilder
import org.apache.logging.log4j.LogManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient

@Configuration
open class ConnectorConfiguration(
  private val settingsConfiguration: SettingsConfiguration,
) {

  @Bean
  open fun webClient(): WebClient {
    return WebClient.builder()
      .filter(ExchangeFilterFunction.ofRequestProcessor {
        log.info(it.url())
        Mono.just(it)
      })
      .clientConnector(ReactorClientHttpConnector(HttpClient.create().compress(true).wiretap(true)))
      .build()
  }

  @Bean
  open fun bamperConnector(): BamperConnector {
    return BamperConnector(
      webClient = webClient(),
      bamperSettings = settingsConfiguration.bamperSettings(),
      authRequestBuilder = authRequestBuilder(),
      authSessionCookieResolver = authSessionCookieResolver(),
      importRequestBuilder = importRequestBuilder()
    )
  }

  @Bean
  open fun authRequestBuilder(): AuthRequestBuilder {
    return AuthRequestBuilder(settingsConfiguration.bamperSettings())
  }

  @Bean
  open fun authSessionCookieResolver(): AuthSessionCookieResolver {
    return AuthSessionCookieResolver(settingsConfiguration.bamperSettings())
  }

  @Bean
  open fun importRequestBuilder(): ImportRequestBuilder {
    return ImportRequestBuilder(settingsConfiguration.importSettings())
  }

  @Bean
  open fun autoPartConnector(): AutoPartConnector {
    val webClient = WebClient
      .builder()
      .baseUrl(
        settingsConfiguration.connectorSettings()
          .domainBaseUrl()
      )
      .build()

    return AutoPartConnector(webClient)
  }

  private companion object {
    private val log = LogManager.getLogger()
  }
}
