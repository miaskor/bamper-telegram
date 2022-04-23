package by.miaskor.bampertelegram

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BamperTelegramApplication

fun main(args: Array<String>) {
  runApplication<BamperTelegramApplication>(*args)
}
