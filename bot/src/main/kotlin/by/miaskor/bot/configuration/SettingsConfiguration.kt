package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.BotSettings
import by.miaskor.bot.configuration.settings.CacheSettings
import by.miaskor.bot.configuration.settings.ConnectorSettings
import by.miaskor.bot.configuration.settings.CreatingAutoPartMessageSettings
import by.miaskor.bot.configuration.settings.CreatingCarMessageSettings
import by.miaskor.bot.configuration.settings.FindingAutoPartMessageSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.configuration.settings.MessageSettings
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
  open fun keyboardSettingsRU(): KeyboardSettings {
    return confProvider.bind("keyboard.ru", KeyboardSettings::class.java)
  }

  @Bean
  open fun keyboardSettingsEN(): KeyboardSettings {
    return confProvider.bind("keyboard.en", KeyboardSettings::class.java)
  }

  @Bean
  open fun messageSettingsRU(): MessageSettings {
    return confProvider.bind("message.ru", MessageSettings::class.java)
  }

  @Bean
  open fun messageSettingsEN(): MessageSettings {
    return confProvider.bind("message.en", MessageSettings::class.java)
  }

  @Bean
  open fun creatingCarMessageSettingsRU(): CreatingCarMessageSettings {
    return confProvider.bind("creating.car.ru", CreatingCarMessageSettings::class.java)
  }

  @Bean
  open fun creatingCarMessageSettingsEN(): CreatingCarMessageSettings {
    return confProvider.bind("creating.car.en", CreatingCarMessageSettings::class.java)
  }

  @Bean
  open fun creatingAutoPartMessageSettingsRU(): CreatingAutoPartMessageSettings {
    return confProvider.bind("creating.auto-part.ru", CreatingAutoPartMessageSettings::class.java)
  }

  @Bean
  open fun creatingAutoPartMessageSettingsEN(): CreatingAutoPartMessageSettings {
    return confProvider.bind("creating.auto-part.en", CreatingAutoPartMessageSettings::class.java)
  }

  @Bean
  open fun findingAutoPartSettingsRU(): FindingAutoPartMessageSettings {
    return confProvider.bind("finding.auto-part.ru", FindingAutoPartMessageSettings::class.java)
  }

  @Bean
  open fun findingAutoPartSettingsEN(): FindingAutoPartMessageSettings {
    return confProvider.bind("finding.auto-part.en", FindingAutoPartMessageSettings::class.java)
  }

  @Bean
  open fun listSettings(): ListSettings {
    return confProvider.bind("list", ListSettings::class.java)
  }
}
