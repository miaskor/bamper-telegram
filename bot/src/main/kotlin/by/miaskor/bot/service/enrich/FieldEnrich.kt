package by.miaskor.bot.service.enrich

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FIELD

@Target(FIELD, CLASS)
@Retention(RUNTIME)
annotation class FieldEnrich(
  val property: String,
  val field: String
)
