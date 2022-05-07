package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.BotSettings
import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.CommandSettings
import by.miaskor.bot.configuration.settings.ConnectorSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.StateSettings
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SettingsConfiguration(
  private val confProvider: ConfigurationProvider
) {

  @Bean
  open fun botSettings(): BotSettings {
    return confProvider.bind("bot", BotSettings::class.java)
  }

  @Bean
  open fun connectorSettings(): ConnectorSettings {
    return confProvider.bind("connector.domain", ConnectorSettings::class.java)
  }

  @Bean
  open fun cacheSettings(): CacheSettings {
    return confProvider.bind("cache", CacheSettings::class.java)
  }

  @Bean
  open fun stateSettings(): StateSettings {
    return confProvider.bind("state", StateSettings::class.java)
  }

  @Bean
  open fun keyboardSettingsRU(): KeyboardSettings {
    return confProvider.bind("keyboard.ru", KeyboardSettings::class.java)
  }

  @Bean
  open fun keyboardSettingsEN(): KeyboardSettings {
    return confProvider.bind("keyboard.en", KeyboardSettings::class.java)
  }

  @Bean
  open fun commandSettingsRU(): CommandSettings {
    return confProvider.bind("command.ru", CommandSettings::class.java)
  }

  @Bean
  open fun commandSettingsEN(): CommandSettings {
    return confProvider.bind("command.en", CommandSettings::class.java)
  }
}
