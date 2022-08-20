package by.miaskor.bot.configuration

import by.miaskor.bot.service.cf4j.LinkPropertiesProvider
import by.miaskor.common.PropertyConfiguration
import org.cfg4j.provider.ConfigurationProvider
import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.context.propertiesprovider.JsonBasedPropertiesProvider
import org.cfg4j.source.context.propertiesprovider.PropertiesProviderSelector
import org.cfg4j.source.context.propertiesprovider.PropertyBasedPropertiesProvider
import org.cfg4j.source.files.FilesConfigurationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Cf4jConfiguration(
  private val propertyConfiguration: PropertyConfiguration,
) {

  @Bean
  open fun classPathConfigurationSource(): FilesConfigurationSource {
    return FilesConfigurationSource(
      { propertyConfiguration.getPropertyPaths() }, PropertiesProviderSelector(
        PropertyBasedPropertiesProvider(), LinkPropertiesProvider(), JsonBasedPropertiesProvider()
      )
    )
  }

  @Bean
  open fun configurationProvider(): ConfigurationProvider {
    return ConfigurationProviderBuilder()
      .withConfigurationSource(classPathConfigurationSource())
      .build()
  }
}
