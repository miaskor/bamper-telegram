package by.miaskor.domain

import by.miaskor.cloud.drive.configuration.CloudDriveConfiguration
import by.miaskor.common.DelegatePropertyConfiguration
import by.miaskor.common.FileCf4jConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import(
  CloudDriveConfiguration::class,
  FileCf4jConfiguration::class,
  DelegatePropertyConfiguration::class,
)
@PropertySource("classpath:domain-application.properties")
@SpringBootApplication
open class DomainApplication

fun main(args: Array<String>) {
  runApplication<DomainApplication>(*args)
}
