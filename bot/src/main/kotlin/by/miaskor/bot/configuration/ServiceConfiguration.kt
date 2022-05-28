package by.miaskor.bot.configuration

import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.ListEntityCache
import by.miaskor.bot.service.ListEntityHandler
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.carstep.CreationCarStepValidation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val connectorConfiguration: ConnectorConfiguration,
  private val settingsConfiguration: SettingsConfiguration,
) {

  @Bean
  open fun telegramClientCache(): TelegramClientCache {
    return TelegramClientCache(
      connectorConfiguration.telegramClientConnector(),
      settingsConfiguration.cacheSettings()
    )
  }

  @Bean
  open fun listEntityCache(): ListEntityCache {
    return ListEntityCache(
      settingsConfiguration.cacheSettings(),
      settingsConfiguration.listSettings()
    )
  }

  @Bean
  open fun listEntityHandler(): ListEntityHandler {
    return ListEntityHandler(
      listEntityCache(),
      settingsConfiguration.listSettings(),
      connectorConfiguration.carConnector(),
      connectorConfiguration.autoPartConnector(),
      telegramClientCache()
    )
  }

  @Bean
  open fun keyboardBuilder(): KeyboardBuilder {
    return KeyboardBuilder(telegramClientCache())
  }

  @Bean
  open fun creationCarStepValidation(): CreationCarStepValidation {
    return CreationCarStepValidation(connectorConfiguration.brandConnector())
  }
}
