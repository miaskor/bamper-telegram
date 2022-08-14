package by.miaskor.configuration

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.configuration.settings.ConnectorSettings
import by.miaskor.connector.BamperConnector
import by.miaskor.domain.api.connector.AutoPartConnector
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
  private val bamperSettings: BamperSettings,
  private val connectorSettings: ConnectorSettings,
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
    return BamperConnector(webClient(), bamperSettings)
  }

  @Bean
  open fun autoPartConnector(): AutoPartConnector {
    val webClient = WebClient
      .builder()
      .baseUrl(connectorSettings.domainBaseUrl())
      .build()
    return AutoPartConnector(webClient)
  }

  private companion object {
    private val log = LogManager.getLogger()
  }
}
