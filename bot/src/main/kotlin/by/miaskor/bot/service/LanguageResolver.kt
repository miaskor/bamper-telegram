package by.miaskor.bot.service

import by.miaskor.bot.domain.Language
import reactor.core.publisher.Mono

object LanguageResolver {

  fun resolve(chatLang: String): Mono<Language> {
    return Mono.just(chatLang)
      .map { Language.findByDomain(chatLang) }
  }
}
