package by.miaskor.cloud.drive.configuration

import by.miaskor.cloud.drive.service.connector.CloudYandexDriveConnector
import by.miaskor.cloud.drive.settings.CloudDriveSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
open class ConnectorConfiguration(
  private val cloudDriveSettings: CloudDriveSettings
) {

  @Bean
  open fun webClient(): WebClient {
    return WebClient.builder()
      .baseUrl(cloudDriveSettings.baseUrl())
      .clientConnector(
        ReactorClientHttpConnector(
          HttpClient.create().followRedirect(true).compress(true)
        )
      )
      .build()
  }

  @Bean
  open fun cloudYandexDriveConnector(): CloudYandexDriveConnector {
    return CloudYandexDriveConnector(
      webClient(),
      cloudDriveSettings
    )
  }
}
