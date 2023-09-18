package by.miaskor.bot.configuration

import by.miaskor.bot.service.cf4j.LinkPropertiesProvider
import by.miaskor.common.FileCf4jConfiguration
import org.cfg4j.source.context.propertiesprovider.JsonBasedPropertiesProvider
import org.cfg4j.source.context.propertiesprovider.PropertiesProviderSelector
import org.cfg4j.source.context.propertiesprovider.PropertyBasedPropertiesProvider
import org.cfg4j.source.files.FilesConfigurationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
open class CustomFileCf4jConfiguration(
  environment: Environment,
) : FileCf4jConfiguration(environment) {

  @Bean
  override fun filesConfigurationSource(): FilesConfigurationSource {
    val propertiesProviderSelector = PropertiesProviderSelector(
      PropertyBasedPropertiesProvider(),
      LinkPropertiesProvider(),
      JsonBasedPropertiesProvider()
    )

    return FilesConfigurationSource(::getPropertyPaths, propertiesProviderSelector)
  }
}
