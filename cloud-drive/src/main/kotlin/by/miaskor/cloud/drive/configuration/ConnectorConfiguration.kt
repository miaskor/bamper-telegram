package by.miaskor.cloud.drive.configuration

import by.miaskor.cloud.drive.configuration.settings.CloudDriveSettings
import by.miaskor.cloud.drive.connector.yandex.YandexCloudDriveConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
open class ConnectorConfiguration(private val cloudDriveSettings: CloudDriveSettings) {

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
  open fun yandexCloudDriveConnector(): YandexCloudDriveConnector {
    return YandexCloudDriveConnector(
      webClient(),
      cloudDriveSettings
    )
  }
}
