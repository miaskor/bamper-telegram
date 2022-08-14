package by.miaskor.configuration

import by.miaskor.configuration.settings.ImportSettings
import by.miaskor.service.AuthorizationService
import by.miaskor.service.ExcelGenerator
import by.miaskor.service.ImportService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val connectorConfiguration: ConnectorConfiguration,
  private val importSettings: ImportSettings,
) {

  @Bean
  open fun authorizationService(): AuthorizationService {
    return AuthorizationService(connectorConfiguration.bamperConnector())
  }

  @Bean
  open fun importService(): ImportService {
    return ImportService(
      connectorConfiguration.bamperConnector(),
      connectorConfiguration.autoPartConnector(),
      excelGenerator()
    )
  }

  @Bean
  open fun excelGenerator(): ExcelGenerator {
    return ExcelGenerator(importSettings)
  }
}
