package by.miaskor.cloud.drive.configuration

import by.miaskor.cloud.drive.configuration.settings.CloudDriveSettings
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SettingsConfiguration(
  private val confProvider: ConfigurationProvider
) {

  @Bean
  open fun cloudDriveSettings(): CloudDriveSettings {
    return confProvider.bind("cloud.drive", CloudDriveSettings::class.java)
  }
}
