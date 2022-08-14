package by.miaskor.configuration

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
        Path(BAMPER_PROPERTY_PATH),
        Path(PROPERTY_CONNECTOR_PATH)
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

    private const val BASE_PATH = "/home/miaskor/Documents/pet-projects/bamper-telegram-properties"
    private const val BAMPER_PROPERTY_PATH = "$BASE_PATH/bamper.yaml"
    private const val PROPERTY_CONNECTOR_PATH = "$BASE_PATH/connector.properties"
  }
}
