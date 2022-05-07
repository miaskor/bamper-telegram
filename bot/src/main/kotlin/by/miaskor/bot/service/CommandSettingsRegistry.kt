package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.CommandSettings
import by.miaskor.bot.domain.Language

class CommandSettingsRegistry(
  private val mapKeyboardSettings: Map<Language, CommandSettings>
) {

  fun lookup(language: Language): CommandSettings {
    return mapKeyboardSettings[language] ?: throw IllegalArgumentException("Language=$language doesn't exists")
  }
}
