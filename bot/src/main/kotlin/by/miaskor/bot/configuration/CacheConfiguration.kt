package by.miaskor.bot.configuration

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.ConstraintAutoPartListEntity
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.FindingAutoPartStep
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.cache.AutoPartByPartNumberListCache
import by.miaskor.bot.service.cache.AutoPartListEntityCache
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.cache.CarListEntityCache
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
  open fun findingAutoPartBuilderCache(): Cache<Long, AbstractStepBuilder<FindingAutoPartStep>> {
    return Cache(settingsConfiguration.cacheSettings())
  }

  @Bean
  open fun carListEntityCache(): AbstractListCache<ListEntity> {
    return CarListEntityCache(
      settingsConfiguration.listSettings(),
      settingsConfiguration.cacheSettings()
    )
  }

  @Bean
  open fun autoPartListEntityCache(): AbstractListCache<ListEntity> {
    return AutoPartListEntityCache(
      settingsConfiguration.listSettings(),
      settingsConfiguration.cacheSettings()
    )
  }

  @Bean
  open fun autoPartByPartNumberListCache(): AbstractListCache<ConstraintAutoPartListEntity> {
    return AutoPartByPartNumberListCache(
      settingsConfiguration.listSettings(),
      settingsConfiguration.cacheSettings()
    )
  }

  @Bean
  @Suppress("UNCHECKED_CAST")
  open fun listEntityCacheRegistry(): ListEntityCacheRegistry {
    return ListEntityCacheRegistry(
      carListEntityCache(),
      autoPartListEntityCache(),
      autoPartByPartNumberListCache() as AbstractListCache<ListEntity>
    )
  }
}
