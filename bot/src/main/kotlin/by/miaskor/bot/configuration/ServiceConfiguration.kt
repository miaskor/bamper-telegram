package by.miaskor.bot.configuration

import by.miaskor.bot.domain.Language.ENGLISH
import by.miaskor.bot.domain.Language.RUSSIAN
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.KeyboardSettingsRegistry
import by.miaskor.bot.service.TelegramClientCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val connectorConfiguration: ConnectorConfiguration,
  private val settingsConfiguration: SettingsConfiguration
) {

  @Bean
  open fun telegramClientCache(): TelegramClientCache {
    return TelegramClientCache(connectorConfiguration.telegramClientConnector())
  }

  @Bean
  open fun keyboardBuilder(): KeyboardBuilder {
    return KeyboardBuilder(keyboardSettingsRegistry(), telegramClientCache())
  }

  @Bean
  open fun keyboardSettingsRegistry(): KeyboardSettingsRegistry {
    return KeyboardSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.keyboardSettingsEN(),
        RUSSIAN to settingsConfiguration.keyboardSettingsRU()
      )
    )
  }
}
