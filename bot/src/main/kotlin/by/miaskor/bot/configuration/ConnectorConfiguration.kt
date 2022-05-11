package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.ConnectorSettings
import by.miaskor.domain.api.connector.StoreHouseConnector
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
open class ConnectorConfiguration(
  private val connectorSettings: ConnectorSettings
) {

  @Bean
  open fun webClient(): WebClient {
    return WebClient.builder()
      .baseUrl(connectorSettings.baseUrl())
      .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
      .clientConnector(ReactorClientHttpConnector(HttpClient.create().compress(true)))
      .build()
  }

  @Bean
  open fun telegramClientConnector(): TelegramClientConnector {
    return TelegramClientConnector(webClient())
  }

  @Bean
  open fun workerTelegramConnector(): WorkerTelegramConnector {
    return WorkerTelegramConnector(webClient())
  }

  @Bean
  open fun storeHouseConnector(): StoreHouseConnector {
    return StoreHouseConnector(webClient())
  }
}
