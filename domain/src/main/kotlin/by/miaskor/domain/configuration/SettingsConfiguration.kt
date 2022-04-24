package by.miaskor.domain.configuration

import by.miaskor.domain.configuration.settings.DataSourceSettings
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SettingsConfiguration(
  private val configurationProvider: ConfigurationProvider
) {

  @Bean
  open fun dataSourceSettings(): DataSourceSettings {
    return configurationProvider.bind("bamper.datasource", DataSourceSettings::class.java)
  }
}
