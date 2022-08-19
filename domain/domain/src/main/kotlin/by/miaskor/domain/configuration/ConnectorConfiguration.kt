package by.miaskor.domain.configuration

import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
open class ConnectorConfiguration(
  private val configurationProvider: ConfigurationProvider,
) {

  @Bean
  open fun telegramWebClient(): WebClient {
    val baseTelegramUrl = configurationProvider.getProperty("bot.baseTelegramUrl", String::class.java)

    return WebClient.builder()
      .baseUrl(baseTelegramUrl)
      .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
      .clientConnector(ReactorClientHttpConnector(HttpClient.create().compress(true)))
      .build()
  }
}
