package by.miaskor.bot.configuration

import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.service.handler.command.BackCommandHandler
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.command.UndefinedCommandHandler
import by.miaskor.bot.service.handler.command.employee.AddEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.employee.EmployeeCommandHandler
import by.miaskor.bot.service.handler.command.employee.EmployeesCommandHandler
import by.miaskor.bot.service.handler.command.employee.ListEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.employee.RemoveEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.language.ChangeLanguageCommandHandler
import by.miaskor.bot.service.handler.command.language.LanguageCommandHandler
import by.miaskor.bot.service.handler.command.storehouse.CreateStoreHouseCommandHandler
import by.miaskor.bot.service.handler.command.storehouse.StoreHouseCommandHandler
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
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
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun stateHandlerRegistry(): BotStateHandlerRegistry {
    return BotStateHandlerRegistry(
      mainMenuHandler(),
      greetingsHandler(),
      employeesMenuHandler()
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
      settingsConfiguration.messageSettingsEN()
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
      listEmployeeCommandHandler(),
      languageCommandHandler(),
      employeeCommandHandler(),
      removeEmployeeCommandHandler(),
      createStoreHouseCommandHandler(),
      storeHouseCommandHandler()
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
      telegramBot(),
      serviceConfiguration.telegramClientCache()
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
  open fun removeEmployeeCommandHandler(): RemoveEmployeeCommandHandler {
    return RemoveEmployeeCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
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

  @Bean
  open fun createStoreHouseCommandHandler(): CreateStoreHouseCommandHandler {
    return CreateStoreHouseCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun storeHouseCommandHandler(): StoreHouseCommandHandler {
    return StoreHouseCommandHandler(
      telegramBot(),
      connectorConfiguration.storeHouseConnector()
    )
  }

  @Bean
  open fun languageCommandHandler(): LanguageCommandHandler {
    return LanguageCommandHandler(
      telegramBot(),
      connectorConfiguration.telegramClientConnector(),
      serviceConfiguration.telegramClientCache(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun employeeCommandHandler(): EmployeeCommandHandler {
    return EmployeeCommandHandler(
      telegramBot(),
      connectorConfiguration.telegramClientConnector(),
      connectorConfiguration.workerTelegramConnector(),
      serviceConfiguration.keyboardBuilder(),
      serviceConfiguration.telegramClientCache(),
    )
  }
}
