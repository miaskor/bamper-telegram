package by.miaskor.bot.configuration

import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.service.handler.command.AddEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.BackCommandHandler
import by.miaskor.bot.service.handler.command.ChangeLanguageCommandHandler
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.command.EmployeesCommandHandler
import by.miaskor.bot.service.handler.command.ListEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.UndefinedCommandHandler
import by.miaskor.bot.service.handler.state.AddingEmployeeHandler
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import by.miaskor.bot.service.handler.state.ChooseLanguageHandler
import by.miaskor.bot.service.handler.state.GreetingsHandler
import by.miaskor.bot.service.handler.state.MenuHandler
import by.miaskor.bot.telegram.Bot
import com.pengrad.telegrambot.TelegramBot
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BotConfiguration(
  private val settingsConfiguration: SettingsConfiguration,
  private val serviceConfiguration: ServiceConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

  @Bean
  open fun telegramBot(): TelegramBot {
    return TelegramBot
      .Builder(settingsConfiguration.botSettings().token())
      .okHttpClient(OkHttpClient())
      .build()
  }

  @Bean
  open fun bot(): Bot {
    return Bot(
      telegramBot(),
      stateHandlerRegistry(),
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun stateHandlerRegistry(): BotStateHandlerRegistry {
    return BotStateHandlerRegistry(
      chooseLanguageHandler(),
      mainMenuHandler(),
      greetingsHandler(),
      employeesMenuHandler(),
      addingEmployeeHandler()
    )
  }

  @Bean
  open fun chooseLanguageHandler(): ChooseLanguageHandler {
    return ChooseLanguageHandler(
      telegramBot(),
      connectorConfiguration.telegramClientConnector(),
      serviceConfiguration.telegramClientCache(),
      settingsConfiguration.stateSettingsEN(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun addingEmployeeHandler(): AddingEmployeeHandler {
    return AddingEmployeeHandler(
      telegramBot(),
      connectorConfiguration.telegramClientConnector(),
      connectorConfiguration.workerTelegramConnector(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun mainMenuHandler(): MenuHandler {
    return MenuHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      MAIN_MENU
    )
  }

  @Bean
  open fun employeesMenuHandler(): MenuHandler {
    return MenuHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      EMPLOYEES_MENU
    )
  }

  @Bean
  open fun greetingsHandler(): GreetingsHandler {
    return GreetingsHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      settingsConfiguration.stateSettingsEN()
    )
  }

  @Bean
  open fun commandHandlerRegistry(): CommandHandlerRegistry {
    return CommandHandlerRegistry(
      changeLanguageCommandHandler(),
      employeesCommandHandler(),
      backCommandHandler(),
      undefinedCommandHandler(),
      addEmployeeCommandHandler(),
      listEmployeeCommandHandler()
    )
  }

  @Bean
  open fun changeLanguageCommandHandler(): ChangeLanguageCommandHandler {
    return ChangeLanguageCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun undefinedCommandHandler(): UndefinedCommandHandler {
    return UndefinedCommandHandler(
      telegramBot()
    )
  }

  @Bean
  open fun employeesCommandHandler(): EmployeesCommandHandler {
    return EmployeesCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun listEmployeeCommandHandler(): ListEmployeeCommandHandler {
    return ListEmployeeCommandHandler(
      telegramBot(),
      connectorConfiguration.telegramClientConnector()
    )
  }

  @Bean
  open fun backCommandHandler(): BackCommandHandler {
    return BackCommandHandler(
      serviceConfiguration.telegramClientCache(),
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun addEmployeeCommandHandler(): AddEmployeeCommandHandler {
    return AddEmployeeCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }
}
