package by.miaskor.configuration

import by.miaskor.service.auth.AuthorizationService
import by.miaskor.service.excel.DefaultExcelGenerator
import by.miaskor.service.excel.DefaultWorkSheetEnricher
import by.miaskor.service.excel.ExcelGenerator
import by.miaskor.service.imprt.AutoPartResponseMapper
import by.miaskor.service.imprt.ImportService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val connectorConfiguration: ConnectorConfiguration,
  private val settingsConfiguration: SettingsConfiguration,
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
      defaultExcelGenerator(),
      AutoPartResponseMapper(settingsConfiguration.importSettings())
    )
  }

  @Bean
  open fun defaultExcelGenerator(): ExcelGenerator {
    return DefaultExcelGenerator(DefaultWorkSheetEnricher())
  }
}
