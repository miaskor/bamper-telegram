package by.miaskor.bampertelegram.configuration

import by.miaskor.bampertelegram.configuration.settings.DataSourceSettings
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SettingsConfiguration(
  private val configurationProvider: ConfigurationProvider
) {

  @Bean
  fun dataSourceSettings(): DataSourceSettings {
    return configurationProvider.bind("bamper.datasource", DataSourceSettings::class.java)
  }
}
