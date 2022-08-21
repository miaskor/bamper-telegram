package by.miaskor.domain

import by.miaskor.cloud.drive.configuration.CloudDriveConfiguration
import by.miaskor.common.PropertyConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import(
  CloudDriveConfiguration::class,
  PropertyConfiguration::class
)
@PropertySource("classpath:domain-application.properties")
@SpringBootApplication
open class DomainApplication

fun main(args: Array<String>) {
  runApplication<DomainApplication>(*args)
}
