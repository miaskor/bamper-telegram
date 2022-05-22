package by.miaskor.domain.configuration

import org.cfg4j.provider.ConfigurationProvider
import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.files.FilesConfigurationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.io.path.Path

@Configuration
open class Cf4jConfiguration {

  @Bean
  open fun classPathConfigurationSource(): FilesConfigurationSource {
    return FilesConfigurationSource {
      listOf(
        Path(DOMAIN_PROPERTY_PATH),
        Path(CLOUD_DRIVE_PROPERTY_PATH),
        Path(TELEGRAM_PROPERTY_PATH)
      )
    }
  }

  @Bean
  open fun configurationProvider(): ConfigurationProvider {
    return ConfigurationProviderBuilder()
      .withConfigurationSource(classPathConfigurationSource())
      .build()
  }

  private companion object {
    private const val DOMAIN_PROPERTY_PATH =
      "/home/mikhailskorohododv/Documents/pet-projects/bamper-telegram-properties/domain.properties"
    private const val CLOUD_DRIVE_PROPERTY_PATH =
      "/home/mikhailskorohododv/Documents/pet-projects/bamper-telegram-properties/cloud-drive.yaml"
    private const val TELEGRAM_PROPERTY_PATH =
      "/home/mikhailskorohododv/Documents/pet-projects/bamper-telegram-properties/bot.yaml"
  }
}
