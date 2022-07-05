package by.miaskor.configuration

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.connector.BamperConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
open class ConnectorConfiguration(
  private val bamperSettings: BamperSettings,
) {

  @Bean
  open fun webClient(): WebClient {
    return WebClient.builder()
      .baseUrl(bamperSettings.bamperUrl())
      .clientConnector(ReactorClientHttpConnector(HttpClient.create().compress(true)))
      .build()
  }

  @Bean
  open fun bamperConnector(): BamperConnector {
    return BamperConnector(webClient(), bamperSettings)
  }
}
