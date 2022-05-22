package by.miaskor.bot.configuration

import by.miaskor.bot.service.cf4j.LinkPropertiesProvider
import org.cfg4j.provider.ConfigurationProvider
import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.context.propertiesprovider.JsonBasedPropertiesProvider
import org.cfg4j.source.context.propertiesprovider.PropertiesProviderSelector
import org.cfg4j.source.context.propertiesprovider.PropertyBasedPropertiesProvider
import org.cfg4j.source.files.FilesConfigurationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.io.path.Path

@Configuration
open class Cf4jConfiguration {

  @Bean
  open fun classPathConfigurationSource(): FilesConfigurationSource {
    return FilesConfigurationSource ({
      listOf(
        Path(PROPERTY_CONNECTOR_PATH),
        Path(PROPERTY_COMMAND_PATH),
        Path(PROPERTY_BOT_PATH),
        Path(PROPERTY_KEYBOARD_PATH),
        Path(PROPERTY_CACHE_PATH),
        Path(PROPERTY_MESSAGE_PATH),
        Path(PROPERTY_CREATING_CAR_MESSAGE_PATH),
        Path(PROPERTY_CREATING_AUTO_PART_PATH),
        Path(PROPERTY_CREATING_AUTO_PART_MESSAGE_PATH),
        Path(PROPERTY_CREATING_CAR_PATH)
      )
    }, PropertiesProviderSelector(
      PropertyBasedPropertiesProvider(), LinkPropertiesProvider(), JsonBasedPropertiesProvider()
    ))
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
    private const val PROPERTY_MESSAGE_PATH = "$COMMON_PATH/message.yaml"
    private const val PROPERTY_CREATING_CAR_MESSAGE_PATH = "$COMMON_PATH/creating-car-message.yaml"
    private const val PROPERTY_CREATING_CAR_PATH = "$COMMON_PATH/creating-car.yaml"
    private const val PROPERTY_COMMAND_PATH = "$COMMON_PATH/command.yaml"
    private const val PROPERTY_CREATING_AUTO_PART_PATH = "$COMMON_PATH/creating-auto-part.yaml"
    private const val PROPERTY_CREATING_AUTO_PART_MESSAGE_PATH = "$COMMON_PATH/creating-auto-part-message.yaml"
  }
}
