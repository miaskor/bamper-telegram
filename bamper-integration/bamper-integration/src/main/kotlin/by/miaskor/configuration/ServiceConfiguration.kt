package by.miaskor.configuration

import by.miaskor.service.AuthorizationService
import by.miaskor.service.ImportService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val connectorConfiguration: ConnectorConfiguration,
) {

  @Bean
  open fun authorizationService(): AuthorizationService {
    return AuthorizationService(connectorConfiguration.bamperConnector())
  }

  @Bean
  open fun importService(): ImportService {
    return ImportService(connectorConfiguration.bamperConnector())
  }
}
