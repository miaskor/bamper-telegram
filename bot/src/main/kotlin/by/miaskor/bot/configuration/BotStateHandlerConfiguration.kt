package by.miaskor.bot.configuration

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.handler.state.BotStateHandler
import by.miaskor.bot.service.handler.state.CreatingAutoPartHandler
import by.miaskor.bot.service.handler.state.CreatingCarHandler
import by.miaskor.bot.service.handler.state.GreetingsHandler
import by.miaskor.bot.service.handler.state.MenuHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BotStateHandlerConfiguration(
  private val botConfiguration: BotConfiguration,
  private val settingsConfiguration: SettingsConfiguration,
  private val serviceConfiguration: ServiceConfiguration,
  private val connectorConfiguration: ConnectorConfiguration
) {

  @Bean
  open fun mainMenuHandler(): BotStateHandler {
    return MenuHandler(
      botConfiguration.telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      BotState.MAIN_MENU
    )
  }

  @Bean
  open fun employeesMenuHandler(): BotStateHandler {
    return MenuHandler(
      botConfiguration.telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      BotState.EMPLOYEES_MENU
    )
  }

  @Bean
  open fun greetingsHandler(): BotStateHandler {
    return GreetingsHandler(
      botConfiguration.telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      settingsConfiguration.messageSettingsEN()
    )
  }

  @Bean
  open fun creatingCarHandler(): BotStateHandler {
    return CreatingCarHandler(
      settingsConfiguration.cacheSettings(),
      botConfiguration.telegramBot(),
      serviceConfiguration.telegramClientCache(),
      serviceConfiguration.keyboardBuilder(),
      botConfiguration.processingStepService(),
      connectorConfiguration.carConnector()
    )
  }

  @Bean
  open fun creatingAutoPartHandler(): CreatingAutoPartHandler {
    return CreatingAutoPartHandler(
      settingsConfiguration.cacheSettings(),
      botConfiguration.telegramBot(),
      serviceConfiguration.telegramClientCache(),
      serviceConfiguration.keyboardBuilder(),
      connectorConfiguration.autoPartConnector(),
      connectorConfiguration.carConnector(),
      connectorConfiguration.carPartConnector()
    )
  }
}
