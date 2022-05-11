package by.miaskor.bot.configuration

import by.miaskor.bot.service.BotStateChanger
import by.miaskor.bot.service.CommandResolver
import by.miaskor.bot.service.LanguageSettingsResolver
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
open class ApplicationConfiguration(
  private val serviceConfiguration: ServiceConfiguration,
  private val commandHandlerRegistry: CommandHandlerRegistry,
  private val botStateHandlerRegistry: BotStateHandlerRegistry,
) {

  @PostConstruct
  fun init() {
    BotStateChanger.telegramClientCache = serviceConfiguration.telegramClientCache()
    LanguageSettingsResolver.messageSettingsRegistry = serviceConfiguration.messageSettingsRegistry()
    LanguageSettingsResolver.keyboardSettingsRegistry = serviceConfiguration.keyboardSettingsRegistry()
    LanguageSettingsResolver.telegramClientCache = serviceConfiguration.telegramClientCache()
    CommandResolver.commandHandlerRegistry = commandHandlerRegistry
    CommandResolver.botStateHandlerRegistry = botStateHandlerRegistry
  }
}
