package by.miaskor

import by.miaskor.common.FileCf4jConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import(
  FileCf4jConfiguration::class
)
@PropertySource("classpath:bamper-integration-application.properties")
@SpringBootApplication
open class BamperIntegrationApplication

fun main(args: Array<String>) {
  runApplication<BamperIntegrationApplication>(*args)
}
