package by.miaskor.bot.service

import by.miaskor.bot.domain.Language
import by.miaskor.bot.service.cache.TelegramClientCache
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

object LanguageSettingsResolver {

  lateinit var messageSettingsRegistry: Registry<KClass<*>, LanguageSettingsRegistry<*>>
  lateinit var telegramClientCache: TelegramClientCache

  fun <R : Any> Mono<Long>.resolveLanguage(clazz: KClass<R>): Mono<R> {
    return this.flatMap {
      Mono.just(it)
        .flatMap(telegramClientCache::getTelegramClient)
        .map { Language.getByDomain(it.chatLanguage) }
        .flatMap { language -> getMessageSettings(language, clazz) }
    }
  }

  private fun <R : Any> getMessageSettings(language: Language, clazz: KClass<R>): Mono<R> {
    return Mono.just(language)
      .map { messageSettingsRegistry.lookup(clazz) }
      .mapNotNull { registry -> registry.lookup(language) }
      .switchIfEmpty(Mono.empty())
      .cast(clazz.java)
  }
}
