package by.miaskor.bot.configuration

import by.miaskor.bot.service.BotStateChanger
import by.miaskor.bot.service.LanguageSettingsResolver
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
open class ApplicationConfiguration(
  private val serviceConfiguration: ServiceConfiguration
) {

  @PostConstruct
  fun init() {
    BotStateChanger.telegramClientCache = serviceConfiguration.telegramClientCache()
    LanguageSettingsResolver.commandSettingsRegistry = serviceConfiguration.commandSettingsRegistry()
    LanguageSettingsResolver.keyboardSettingsRegistry = serviceConfiguration.keyboardSettingsRegistry()
    LanguageSettingsResolver.stateSettingsRegistry = serviceConfiguration.stateSettingsRegistry()
    LanguageSettingsResolver.telegramClientCache = serviceConfiguration.telegramClientCache()
  }
}
