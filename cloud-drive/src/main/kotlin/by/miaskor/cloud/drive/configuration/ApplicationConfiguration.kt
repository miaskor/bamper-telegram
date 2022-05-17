package by.miaskor.cloud.drive.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(
  ConnectorConfiguration::class,
  ServiceConfiguration::class,
  SettingsConfiguration::class
)
@Configuration
open class ApplicationConfiguration
