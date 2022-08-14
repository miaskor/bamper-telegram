package by.miaskor.configuration

import by.miaskor.configuration.settings.BamperSettings
import by.miaskor.configuration.settings.ConnectorSettings
import by.miaskor.configuration.settings.ImportSettings
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SettingsConfiguration(
  private val configurationProvider: ConfigurationProvider,
) {

  @Bean
  open fun bamperSettings(): BamperSettings {
    return configurationProvider.bind("bamper.integration", BamperSettings::class.java)
  }

  @Bean
  open fun importSettings(): ImportSettings {
    return configurationProvider.bind("bamper.import", ImportSettings::class.java)
  }

  @Bean
  open fun connectorSettings(): ConnectorSettings {
    return configurationProvider.bind("connector", ConnectorSettings::class.java)
  }

}
