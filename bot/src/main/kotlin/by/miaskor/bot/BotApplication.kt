package by.miaskor.bot

import by.miaskor.common.DelegatePropertyConfiguration
import by.miaskor.common.PropertyConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource

@Import(
  PropertyConfiguration::class,
  DelegatePropertyConfiguration::class
)
@PropertySource("classpath:bot-application.properties")
@SpringBootApplication
open class BotApplication

fun main(args: Array<String>) {
  runApplication<BotApplication>(*args)
}
