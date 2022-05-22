package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Language

class StateSettingsRegistry(
  private val stateSettingsMap: Map<Language, MessageSettings>
) {

  fun lookup(language: Language): MessageSettings {
    return stateSettingsMap[language] ?: throw IllegalArgumentException("Language=$language doesn't exists")
  }
}
