package by.miaskor.bot.configuration

import by.miaskor.bot.domain.Language.ENGLISH
import by.miaskor.bot.domain.Language.RUSSIAN
import by.miaskor.bot.service.KeyboardSettingsRegistry
import by.miaskor.bot.service.MessageSettingsRegistry
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RegistryConfiguration(
  private val settingsConfiguration: SettingsConfiguration,
  private val commandHandlerConfiguration: CommandHandlerConfiguration,
  private val botStateHandlerConfiguration: BotStateHandlerConfiguration
) {

  @Bean
  open fun botStateHandlerRegistry(): BotStateHandlerRegistry {
    return BotStateHandlerRegistry(
      botStateHandlerConfiguration.mainMenuHandler(),
      botStateHandlerConfiguration.greetingsHandler(),
      botStateHandlerConfiguration.employeesMenuHandler()
    )
  }

  @Bean
  open fun commandHandlerRegistry(): CommandHandlerRegistry {
    return CommandHandlerRegistry(
      commandHandlerConfiguration.changeLanguageCommandHandler(),
      commandHandlerConfiguration.employeesCommandHandler(),
      commandHandlerConfiguration.backCommandHandler(),
      commandHandlerConfiguration.undefinedCommandHandler(),
      commandHandlerConfiguration.addEmployeeCommandHandler(),
      commandHandlerConfiguration.listEmployeeCommandHandler(),
      commandHandlerConfiguration.languageCommandHandler(),
      commandHandlerConfiguration.employeeCommandHandler(),
      commandHandlerConfiguration.removeEmployeeCommandHandler(),
      commandHandlerConfiguration.createStoreHouseCommandHandler(),
      commandHandlerConfiguration.storeHouseCommandHandler()
    )
  }

  @Bean
  open fun keyboardSettingsRegistry(): KeyboardSettingsRegistry {
    return KeyboardSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.keyboardSettingsEN(),
        RUSSIAN to settingsConfiguration.keyboardSettingsRU()
      )
    )
  }

  @Bean
  open fun messageSettingsRegistry(): MessageSettingsRegistry {
    return MessageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.messageSettingsEN(),
        RUSSIAN to settingsConfiguration.messageSettingsRU()
      )
    )
  }
}
