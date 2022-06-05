package by.miaskor.bot.configuration

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.ListEntityType
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.cache.ListEntityCacheRegistry
import by.miaskor.bot.service.cache.TelegramClientCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CacheConfiguration(
  private val settingsConfiguration: SettingsConfiguration,
  private val connectorConfiguration: ConnectorConfiguration
) {

  @Bean
  open fun telegramClientCache(): TelegramClientCache {
    return TelegramClientCache(
      connectorConfiguration.telegramClientConnector(),
      settingsConfiguration.cacheSettings()
    )
  }

  @Bean
  open fun carBuilderCache(): Cache<Long, AbstractStepBuilder<CreatingCarStep>> {
    return Cache(settingsConfiguration.cacheSettings())
  }

  @Bean
  open fun autoPartBuilderCache(): Cache<Long, AbstractStepBuilder<CreatingAutoPartStep>> {
    return Cache(settingsConfiguration.cacheSettings())
  }

  @Bean
  open fun carListEntityCache(): AbstractListCache {
    return object : AbstractListCache(
      settingsConfiguration.cacheSettings(),
      settingsConfiguration.listSettings()
    ) {
      override fun listEntityType() = ListEntityType.CAR
    }
  }

  @Bean
  open fun autoPartListEntityCache(): AbstractListCache {
    return object : AbstractListCache(
      settingsConfiguration.cacheSettings(),
      settingsConfiguration.listSettings()
    ) {
      override fun listEntityType() = ListEntityType.AUTO_PART
    }
  }

  @Bean
  open fun listEntityCacheRegistry(): ListEntityCacheRegistry {
    return ListEntityCacheRegistry(
      carListEntityCache(),
      autoPartListEntityCache(),
    )
  }
}