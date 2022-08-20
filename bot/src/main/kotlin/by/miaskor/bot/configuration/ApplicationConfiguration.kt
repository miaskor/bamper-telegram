package by.miaskor.bot.configuration

import by.miaskor.bot.domain.Command
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.FindingAutoPartStep
import by.miaskor.bot.service.BotStateChanger
import by.miaskor.bot.service.CommandResolver
import by.miaskor.bot.service.LanguageSettingsResolver
import by.miaskor.bot.service.MessageSender
import by.miaskor.bot.service.enrich.FieldEnricher
import by.miaskor.bot.service.handler.list.ListEntitySender
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
open class ApplicationConfiguration(
  private val serviceConfiguration: ServiceConfiguration,
  private val registryConfiguration: RegistryConfiguration,
  private val commandHandlerConfiguration: CommandHandlerConfiguration,
  private val botStateHandlerConfiguration: BotStateHandlerConfiguration,
  private val confProvider: ConfigurationProvider,
  private val cacheConfiguration: CacheConfiguration,
  private val botConfiguration: BotConfiguration,
  private val settingsConfiguration: SettingsConfiguration,
) {

  @PostConstruct
  fun init() {
    BotStateChanger.telegramClientCache = cacheConfiguration.telegramClientCache()
    BotStateChanger.listEntityCacheRegistry = cacheConfiguration.listEntityCacheRegistry()
    BotStateChanger.carBuilderCache = cacheConfiguration.carBuilderCache()
    LanguageSettingsResolver.apply {
      messageSettingsRegistry = registryConfiguration.compositeMessageSettingsRegistry()
      telegramClientCache = cacheConfiguration.telegramClientCache()
    }
    CommandResolver.apply {
      commandHandlerRegistry = commandHandlerConfiguration.commandHandlerRegistry()
      botStateHandlerRegistry = botStateHandlerConfiguration.botStateHandlerRegistry()
      callBackCommandHandler = commandHandlerConfiguration.callBackCommandHandler()
    }
    FieldEnricher.apply {
      configurationProvider = confProvider
      enrich(
        Command::class.java,
        CreatingCarStep::class.java,
        CreatingAutoPartStep::class.java,
        FindingAutoPartStep::class.java
      )
    }

    MessageSender.apply {
      this.telegramBot = botConfiguration.telegramBot()
      keyboardBuilder = serviceConfiguration.keyboardBuilder()
    }

    ListEntitySender.executorSettings = settingsConfiguration.executorSettings()
  }
}
