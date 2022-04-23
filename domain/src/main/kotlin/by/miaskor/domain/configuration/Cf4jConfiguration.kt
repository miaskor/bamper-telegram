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
    return FilesConfigurationSource { listOf(Path(PROPERTY_PATH)) }
  }

  @Bean
  open fun configurationProvider(): ConfigurationProvider {
    return ConfigurationProviderBuilder()
      .withConfigurationSource(classPathConfigurationSource())
      .build()
  }

  private companion object {
    private const val PROPERTY_PATH = "/home/mikhailskorohododv/Pictures/application.properties"
  }
}
