package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.Language

class StateSettingsRegistry(
  private val stateSettingsMap: Map<Language, StateSettings>
) {

  fun lookup(language: Language): StateSettings {
    return stateSettingsMap[language] ?: throw IllegalArgumentException("Language=$language doesn't exists")
  }
}
