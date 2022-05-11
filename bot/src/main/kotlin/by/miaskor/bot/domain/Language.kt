package by.miaskor.bot.domain

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

enum class Language(val domain: String, vararg val languages: String) {
  ENGLISH("EN", "English", "Английский"),
  RUSSIAN("RU", "Русский", "Russian");

  companion object {
    fun getByDomain(domain: String): Mono<Language> {
      return Flux.fromArray(values())
        .filter { it.domain == domain }
        .next()
        .defaultIfEmpty(ENGLISH)
    }

    fun getByFullLanguage(fullLanguage: String): Mono<Language> {
      return Flux.fromArray(values())
        .filter { it.languages.contains(fullLanguage) }
        .next()
        .defaultIfEmpty(ENGLISH)
    }
  }
}
