package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.ConnectorSettings
import by.miaskor.connector.BamperIntegrationConnector
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.connector.BamperClientConnector
import by.miaskor.domain.api.connector.BrandConnector
import by.miaskor.domain.api.connector.CarConnector
import by.miaskor.domain.api.connector.CarPartConnector
import by.miaskor.domain.api.connector.StoreHouseConnector
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerStoreHouseConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
open class ConnectorConfiguration(
  private val connectorSettings: ConnectorSettings,
) {

  @Bean
  open fun domainWebClient(): WebClient {
    val size = 16 * 1024 * 1024
    val strategies = ExchangeStrategies.builder()
      .codecs { codecs: ClientCodecConfigurer ->
        codecs.defaultCodecs().maxInMemorySize(size)
      }
      .build()

    return WebClient.builder()
      .exchangeStrategies(strategies)
      .baseUrl(connectorSettings.domainBaseUrl())
      .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
      .clientConnector(ReactorClientHttpConnector(HttpClient.create().compress(true)))
      .build()
  }

  @Bean
  open fun bamperIntegrationWebClient(): WebClient {

    return WebClient.builder()
      .baseUrl(connectorSettings.bamperIntegrationBaseUrl())
      .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
      .clientConnector(ReactorClientHttpConnector(HttpClient.create().compress(true)))
      .build()
  }

  @Bean
  open fun telegramClientConnector(): TelegramClientConnector {
    return TelegramClientConnector(domainWebClient())
  }

  @Bean
  open fun workerTelegramConnector(): WorkerTelegramConnector {
    return WorkerTelegramConnector(domainWebClient())
  }

  @Bean
  open fun storeHouseConnector(): StoreHouseConnector {
    return StoreHouseConnector(domainWebClient())
  }

  @Bean
  open fun brandConnector(): BrandConnector {
    return BrandConnector(domainWebClient())
  }

  @Bean
  open fun carConnector(): CarConnector {
    return CarConnector(domainWebClient())
  }

  @Bean
  open fun autoPartConnector(): AutoPartConnector {
    return AutoPartConnector(domainWebClient())
  }

  @Bean
  open fun bamperClientConnector(): BamperClientConnector {
    return BamperClientConnector(domainWebClient())
  }

  @Bean
  open fun carPartConnector(): CarPartConnector {
    return CarPartConnector(domainWebClient())
  }

  @Bean
  open fun workerStoreHouseConnector(): WorkerStoreHouseConnector {
    return WorkerStoreHouseConnector(domainWebClient())
  }

  @Bean
  open fun bamperIntegrationConnector(): BamperIntegrationConnector {
    return BamperIntegrationConnector(bamperIntegrationWebClient())
  }
}
