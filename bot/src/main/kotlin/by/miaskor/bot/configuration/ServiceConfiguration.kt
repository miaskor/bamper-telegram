package by.miaskor.bot.configuration

import by.miaskor.bot.domain.ListEntityType.AUTO_PART
import by.miaskor.bot.domain.ListEntityType.CAR
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.ListEntityHandler
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.cache.ListEntityCacheRegistry
import by.miaskor.bot.service.cache.TelegramClientCache
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
  open fun listEntityHandler(): ListEntityHandler {
    return ListEntityHandler(
      listEntityCacheRegistry(),
      settingsConfiguration.listSettings(),
      connectorConfiguration.carConnector(),
      connectorConfiguration.autoPartConnector(),
      telegramClientCache()
    )
  }

  @Bean
  open fun listEntityCacheRegistry(): ListEntityCacheRegistry {
    return ListEntityCacheRegistry(
      carListEntityCache(),
      autoPartListEntityCache(),
    )
  }

  @Bean
  open fun carListEntityCache(): AbstractListCache {
    return object : AbstractListCache(
      settingsConfiguration.cacheSettings(),
      settingsConfiguration.listSettings()
    ) {
      override fun listEntityType() = CAR
    }
  }

  @Bean
  open fun autoPartListEntityCache(): AbstractListCache {
    return object : AbstractListCache(
      settingsConfiguration.cacheSettings(),
      settingsConfiguration.listSettings()
    ) {
      override fun listEntityType() = AUTO_PART
    }
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
