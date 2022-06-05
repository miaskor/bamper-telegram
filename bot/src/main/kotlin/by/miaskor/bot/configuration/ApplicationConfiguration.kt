package by.miaskor.bot.configuration

import by.miaskor.bot.domain.Command
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.service.BotStateChanger
import by.miaskor.bot.service.CommandResolver
import by.miaskor.bot.service.LanguageSettingsResolver
import by.miaskor.bot.service.MessageSender
import by.miaskor.bot.service.enrich.FieldEnricher
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
  private val botConfiguration: BotConfiguration
) {

  @PostConstruct
  fun init() {
    BotStateChanger.telegramClientCache = cacheConfiguration.telegramClientCache()
    BotStateChanger.listEntityCacheRegistry = cacheConfiguration.listEntityCacheRegistry()
    BotStateChanger.carBuilderCache = cacheConfiguration.carBuilderCache()
    LanguageSettingsResolver.apply {
      messageSettingsRegistry = registryConfiguration.messageSettingsRegistry()
      keyboardSettingsRegistry = registryConfiguration.keyboardSettingsRegistry()
      telegramClientCache = cacheConfiguration.telegramClientCache()
      creatingCarMessageSettingsRegistry = registryConfiguration.creatingCarMessageSettingsRegistry()
      creatingAutoPartMessageSettingsRegistry = registryConfiguration.creatingAutoPartMessageSettingsRegistry()
    }
    CommandResolver.apply {
      commandHandlerRegistry = commandHandlerConfiguration.commandHandlerRegistry()
      botStateHandlerRegistry = botStateHandlerConfiguration.botStateHandlerRegistry()
      callBackCommandHandler = commandHandlerConfiguration.callBackCommandHandler()
    }
    FieldEnricher.apply {
      configurationProvider = confProvider
      enrich(Command::class.java)
      enrich(CreatingCarStep::class.java)
      enrich(CreatingAutoPartStep::class.java)
    }

    MessageSender.apply {
      this.telegramBot = botConfiguration.telegramBot()
      keyboardBuilder = serviceConfiguration.keyboardBuilder()
    }
  }
}
