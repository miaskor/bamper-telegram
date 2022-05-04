package by.miaskor.bot.configuration

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
        Path(PROPERTY_CONNECTOR_PATH),
        Path(PROPERTY_BOT_PATH),
        Path(PROPERTY_KEYBOARD_PATH),
        Path(PROPERTY_CACHE_PATH),
        Path(PROPERTY_STATE_PATH)
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
    private const val COMMON_PATH = "/home/mikhailskorohododv/Documents/pet-projects/bamper-telegram-properties"
    private const val PROPERTY_BOT_PATH = "$COMMON_PATH/bot.yaml"
    private const val PROPERTY_KEYBOARD_PATH = "$COMMON_PATH/keyboard.yaml"
    private const val PROPERTY_CONNECTOR_PATH = "$COMMON_PATH/connector.properties"
    private const val PROPERTY_CACHE_PATH = "$COMMON_PATH/cache.yaml"
    private const val PROPERTY_STATE_PATH = "$COMMON_PATH/state.yaml"
  }
}
