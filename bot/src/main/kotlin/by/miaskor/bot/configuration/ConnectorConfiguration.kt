package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.ConnectorSettings
import by.miaskor.domain.api.connector.AutoPartConnector
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
  private val connectorSettings: ConnectorSettings
) {

  @Bean
  open fun webClient(): WebClient {
    val size = 16 * 1024 * 1024
    val strategies = ExchangeStrategies.builder()
      .codecs { codecs: ClientCodecConfigurer ->
        codecs.defaultCodecs().maxInMemorySize(size)
      }
      .build()

    return WebClient.builder()
      .exchangeStrategies(strategies)
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

  @Bean
  open fun brandConnector(): BrandConnector {
    return BrandConnector(webClient())
  }

  @Bean
  open fun carConnector(): CarConnector {
    return CarConnector(webClient())
  }

  @Bean
  open fun autoPartConnector(): AutoPartConnector {
    return AutoPartConnector(webClient())
  }

  @Bean
  open fun carPartConnector(): CarPartConnector {
    return CarPartConnector(webClient())
  }

  @Bean
  open fun workerStoreHouseConnector(): WorkerStoreHouseConnector {
    return WorkerStoreHouseConnector(webClient())
  }
}
