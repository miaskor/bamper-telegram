package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.CreatingAutoPartMessageSettings
import by.miaskor.bot.configuration.settings.CreatingCarMessageSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Language.ENGLISH
import by.miaskor.bot.domain.Language.RUSSIAN
import by.miaskor.bot.service.LanguageSettingsRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RegistryConfiguration(
  private val settingsConfiguration: SettingsConfiguration
) {

  @Bean
  open fun keyboardSettingsRegistry(): LanguageSettingsRegistry<KeyboardSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.keyboardSettingsEN(),
        RUSSIAN to settingsConfiguration.keyboardSettingsRU()
      )
    )
  }

  @Bean
  open fun messageSettingsRegistry(): LanguageSettingsRegistry<MessageSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.messageSettingsEN(),
        RUSSIAN to settingsConfiguration.messageSettingsRU()
      )
    )
  }

  @Bean
  open fun creatingCarMessageSettingsRegistry(): LanguageSettingsRegistry<CreatingCarMessageSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.creatingCarMessageSettingsEN(),
        RUSSIAN to settingsConfiguration.creatingCarMessageSettingsRU()
      )
    )
  }

  @Bean
  open fun creatingAutoPartMessageSettingsRegistry(): LanguageSettingsRegistry<CreatingAutoPartMessageSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.creatingAutoPartMessageSettingsEN(),
        RUSSIAN to settingsConfiguration.creatingAutoPartMessageSettingsRU()
      )
    )
  }
}
