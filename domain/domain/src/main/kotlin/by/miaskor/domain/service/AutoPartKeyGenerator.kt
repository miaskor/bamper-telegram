package by.miaskor.domain.service

import java.util.UUID

object AutoPartKeyGenerator {

  fun generate(): String{
    return UUID.randomUUID().toString()
  }
}
