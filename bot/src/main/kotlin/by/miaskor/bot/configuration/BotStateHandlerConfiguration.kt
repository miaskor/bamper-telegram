package by.miaskor.bot.configuration

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.service.handler.state.BotStateHandler
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import by.miaskor.bot.service.handler.state.CreatingAutoPartHandler
import by.miaskor.bot.service.handler.state.CreatingCarHandler
import by.miaskor.bot.service.handler.state.GreetingsHandler
import by.miaskor.bot.service.handler.state.MenuHandler
import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BotStateHandlerConfiguration(
  private val telegramBot: TelegramBot,
  private val settingsConfiguration: SettingsConfiguration,
  private val serviceConfiguration: ServiceConfiguration,
  private val cacheConfiguration: CacheConfiguration,
  private val connectorConfiguration: ConnectorConfiguration
) {

  @Bean
  open fun botStateHandlerRegistry(): BotStateHandlerRegistry {
    return BotStateHandlerRegistry(
      mainMenuHandler(),
      greetingsHandler(),
      employeesMenuHandler(),
      creatingCarHandler(),
      creatingAutoPartHandler()
    )
  }

  @Bean
  open fun mainMenuHandler(): BotStateHandler {
    return MenuHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      BotState.MAIN_MENU
    )
  }

  @Bean
  open fun employeesMenuHandler(): BotStateHandler {
    return MenuHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      BotState.EMPLOYEES_MENU
    )
  }

  @Bean
  open fun greetingsHandler(): BotStateHandler {
    return GreetingsHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      settingsConfiguration.messageSettingsEN()
    )
  }

  @Bean
  open fun creatingCarHandler(): BotStateHandler {
    return CreatingCarHandler(
      cacheConfiguration.carBuilderCache(),
      serviceConfiguration.creatingCarStepKeyboardBuilder(),
      serviceConfiguration.creatingCarStepMessageResolver(),
      telegramBot,
      serviceConfiguration.processingCarStepService(),
      cacheConfiguration.telegramClientCache(),
      connectorConfiguration.carConnector(),
    )
  }

  @Bean
  open fun creatingAutoPartHandler(): CreatingAutoPartHandler {
    return CreatingAutoPartHandler(
      cacheConfiguration.autoPartBuilderCache(),
      serviceConfiguration.creatingAutoPartStepKeyboardBuilder(),
      serviceConfiguration.creatingAutoPartStepMessageResolver(),
      telegramBot,
      serviceConfiguration.processingAutoPartStepService(),
      cacheConfiguration.telegramClientCache(),
      connectorConfiguration.autoPartConnector()
    )
  }
}
