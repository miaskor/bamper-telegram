package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.Language
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

object LanguageSettingsResolver {

  lateinit var keyboardSettingsRegistry: KeyboardSettingsRegistry
  lateinit var messageSettingsRegistry: MessageSettingsRegistry
  lateinit var telegramClientCache: TelegramClientCache

  fun <R: Any> Mono<Long>.resolveLanguage(clazz: KClass<R>): Mono<R> {
    val language = this.flatMap {
      Mono.just(it)
        .flatMap(telegramClientCache::getTelegramClient)
        .flatMap { Language.getByDomain(it.chatLanguage) }
    }
    if (clazz == KeyboardSettings::class) {
      return language
        .map(keyboardSettingsRegistry::lookup)
        .cast(clazz.java)
    }
    if (clazz == MessageSettings::class) {
      return language
        .map(messageSettingsRegistry::lookup)
        .cast(clazz.java)
    }

    return Mono.empty()
  }
}
