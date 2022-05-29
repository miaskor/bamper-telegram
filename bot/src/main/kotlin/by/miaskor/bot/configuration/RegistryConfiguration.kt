package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.CreatingAutoPartMessageSettings
import by.miaskor.bot.configuration.settings.CreatingCarMessageSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Language.ENGLISH
import by.miaskor.bot.domain.Language.RUSSIAN
import by.miaskor.bot.service.CallBackQueryHandlerRegistry
import by.miaskor.bot.service.LanguageSettingsRegistry
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RegistryConfiguration(
  private val settingsConfiguration: SettingsConfiguration,
  private val commandHandlerConfiguration: CommandHandlerConfiguration,
  private val botStateHandlerConfiguration: BotStateHandlerConfiguration,
  private val serviceConfiguration: ServiceConfiguration
) {

  @Bean
  open fun botStateHandlerRegistry(): BotStateHandlerRegistry {
    return BotStateHandlerRegistry(
      botStateHandlerConfiguration.mainMenuHandler(),
      botStateHandlerConfiguration.greetingsHandler(),
      botStateHandlerConfiguration.employeesMenuHandler(),
      botStateHandlerConfiguration.creatingCarHandler(),
      botStateHandlerConfiguration.creatingAutoPartHandler()
    )
  }

  @Bean
  open fun commandHandlerRegistry(): CommandHandlerRegistry {
    return CommandHandlerRegistry(
      commandHandlerConfiguration.addEmployeeToStoreHouseCommandHandler(),
      commandHandlerConfiguration.changeLanguageCommandHandler(),
      commandHandlerConfiguration.employeesCommandHandler(),
      commandHandlerConfiguration.backCommandHandler(),
      commandHandlerConfiguration.undefinedCommandHandler(),
      commandHandlerConfiguration.addEmployeeCommandHandler(),
      commandHandlerConfiguration.listEmployeeCommandHandler(),
      commandHandlerConfiguration.languageCommandHandler(),
      commandHandlerConfiguration.employeeCommandHandler(),
      commandHandlerConfiguration.addingEmployeeToStoreHouseCommandHandler(),
      commandHandlerConfiguration.removeEmployeeCommandHandler(),
      commandHandlerConfiguration.createStoreHouseCommandHandler(),
      commandHandlerConfiguration.storeHouseCommandHandler(),
      commandHandlerConfiguration.chooseStoreHouseCommandHandler(),
      commandHandlerConfiguration.selectCertainStoreHouseCommandHandler(),
      commandHandlerConfiguration.createAutoPartCommandHandler(),
      commandHandlerConfiguration.listCarCommandHandler(),
      commandHandlerConfiguration.deleteAutoPartCommandHandler(),
      commandHandlerConfiguration.deleteCarCommandHandler(),
      commandHandlerConfiguration.listAutoPartCommandHandler(),
      commandHandlerConfiguration.deleteCarsCommandHandler(),
      commandHandlerConfiguration.deleteAutoPartsCommandHandler(),
      commandHandlerConfiguration.createCarCommandHandler()
    )
  }

  @Bean
  open fun callBackQueryHandlerRegistry(): CallBackQueryHandlerRegistry {
    return CallBackQueryHandlerRegistry(serviceConfiguration.listEntityHandler())
  }

  @Bean
  open fun keyboardSettingsRegistry(): LanguageSettingsRegistry<KeyboardSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.keyboardSettingsEN(),
        RUSSIAN to settingsConfiguration.keyboardSettingsRU()
      )
    )
  }

  @Bean
  open fun messageSettingsRegistry(): LanguageSettingsRegistry<MessageSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.messageSettingsEN(),
        RUSSIAN to settingsConfiguration.messageSettingsRU()
      )
    )
  }

  @Bean
  open fun creatingCarMessageSettingsRegistry(): LanguageSettingsRegistry<CreatingCarMessageSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.creatingCarMessageSettingsEN(),
        RUSSIAN to settingsConfiguration.creatingCarMessageSettingsRU()
      )
    )
  }

  @Bean
  open fun creatingAutoPartMessageSettingsRegistry(): LanguageSettingsRegistry<CreatingAutoPartMessageSettings> {
    return LanguageSettingsRegistry(
      mapOf(
        ENGLISH to settingsConfiguration.creatingAutoPartMessageSettingsEN(),
        RUSSIAN to settingsConfiguration.creatingAutoPartMessageSettingsRU()
      )
    )
  }
}
