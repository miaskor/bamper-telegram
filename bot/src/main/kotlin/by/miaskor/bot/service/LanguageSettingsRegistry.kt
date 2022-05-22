package by.miaskor.bot.service

import by.miaskor.bot.domain.Language

class LanguageSettingsRegistry<T>(
  private val mapSettings: Map<Language, T>
) {

  fun lookup(language: Language): T {
    return mapSettings[language] ?: throw IllegalArgumentException("Language=$language doesn't exists")
  }
}
