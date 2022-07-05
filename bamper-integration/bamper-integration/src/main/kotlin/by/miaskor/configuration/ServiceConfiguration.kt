package by.miaskor.configuration

import by.miaskor.service.AuthorizationService
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
}
