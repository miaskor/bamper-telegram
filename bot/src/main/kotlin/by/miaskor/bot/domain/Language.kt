package by.miaskor.bot.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

enum class Language(val domain: String, val language: String) {
  ENGLISH("EN", "English"),
  RUSSIAN("RU", "Русский");

  companion object {
    fun getByDomain(domain: String): Mono<Language> {
      return Flux.fromArray(values())
        .filter { it.domain == domain }
        .next()
        .defaultIfEmpty(ENGLISH)
    }

    fun getByFullLanguage(fullLanguage: String): Mono<Language> {
      return Flux.fromArray(values())
        .filter { it.language == fullLanguage }
        .next()
        .defaultIfEmpty(ENGLISH)
    }

    fun isLanguageExists(language: String): Boolean {
      return values().map { it.domain }
        .plus(values().map { it.language })
        .map { it.lowercase() }
        .contains(language.lowercase())
    }
  }
}
