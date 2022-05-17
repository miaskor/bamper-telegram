package by.miaskor.bot.configuration

import by.miaskor.bot.service.BotStateChanger
import by.miaskor.bot.service.CommandResolver
import by.miaskor.bot.service.LanguageSettingsResolver
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
open class ApplicationConfiguration(
  private val serviceConfiguration: ServiceConfiguration,
  private val registryConfiguration: RegistryConfiguration
) {

  @PostConstruct
  fun init() {
    BotStateChanger.telegramClientCache = serviceConfiguration.telegramClientCache()
    LanguageSettingsResolver.messageSettingsRegistry = registryConfiguration.messageSettingsRegistry()
    LanguageSettingsResolver.keyboardSettingsRegistry = registryConfiguration.keyboardSettingsRegistry()
    LanguageSettingsResolver.telegramClientCache = serviceConfiguration.telegramClientCache()
    LanguageSettingsResolver.creatingCarMessageSettingsRegistry = registryConfiguration.creatingCarMessageSettingsRegistry()
    CommandResolver.commandHandlerRegistry = registryConfiguration.commandHandlerRegistry()
    CommandResolver.botStateHandlerRegistry = registryConfiguration.botStateHandlerRegistry()
  }
}
