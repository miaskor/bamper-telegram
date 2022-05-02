package by.miaskor.domain.configuration

import by.miaskor.domain.configuration.settings.DataSourceSettings
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SettingsConfiguration(
  private val confProvider: ConfigurationProvider
) {

  @Bean
  open fun dataSourceSettings(): DataSourceSettings {
    return confProvider.bind("bamper.datasource", DataSourceSettings::class.java)
  }
}
