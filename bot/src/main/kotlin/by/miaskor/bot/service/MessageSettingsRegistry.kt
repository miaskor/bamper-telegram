package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Language

class MessageSettingsRegistry(
  private val messageSettingsMap: Map<Language, MessageSettings>
) {

  fun lookup(language: Language): MessageSettings {
    return messageSettingsMap[language] ?: throw IllegalArgumentException("Language=$language doesn't exists")
  }
}
