package by.miaskor.configuration

import by.miaskor.configuration.settings.BamperSettings
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SettingsConfiguration(
  private val configurationProvider: ConfigurationProvider,
) {

  @Bean
  open fun bamperSettings(): BamperSettings {
    return configurationProvider.bind("bamper-integration", BamperSettings::class.java)
  }
}
