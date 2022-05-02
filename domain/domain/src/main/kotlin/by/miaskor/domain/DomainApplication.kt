package by.miaskor.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class DomainApplication

fun main(args: Array<String>) {
  runApplication<DomainApplication>(*args)
}
