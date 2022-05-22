package by.miaskor.domain

import by.miaskor.cloud.drive.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(ApplicationConfiguration::class)
@SpringBootApplication
open class DomainApplication

fun main(args: Array<String>) {
  runApplication<DomainApplication>(*args)
}
