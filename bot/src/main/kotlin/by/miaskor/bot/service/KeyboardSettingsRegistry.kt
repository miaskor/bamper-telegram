package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.Language

class KeyboardSettingsRegistry(
  private val mapKeyboardSettings: Map<Language, KeyboardSettings>
) {

  fun lookup(language: Language): KeyboardSettings {
    return mapKeyboardSettings[language] ?: throw IllegalArgumentException("Language=$language doesn't exists")
  }
}
