package by.miaskor.bot.configuration

import by.miaskor.bot.service.handler.command.BackCommandHandler
import by.miaskor.bot.service.handler.command.CommandHandler
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
import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CommandHandlerConfiguration(
  private val telegramBot: TelegramBot,
  private val serviceConfiguration: ServiceConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

  @Bean
  open fun changeLanguageCommandHandler(): CommandHandler {
    return ChangeLanguageCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun undefinedCommandHandler(): CommandHandler {
    return UndefinedCommandHandler(
      telegramBot,
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun employeesCommandHandler(): CommandHandler {
    return EmployeesCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun listEmployeeCommandHandler(): CommandHandler {
    return ListEmployeeCommandHandler(
      telegramBot,
      connectorConfiguration.telegramClientConnector(),
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun removeEmployeeCommandHandler(): CommandHandler {
    return RemoveEmployeeCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun backCommandHandler(): CommandHandler {
    return BackCommandHandler(
      serviceConfiguration.telegramClientCache(),
      telegramBot,
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun addEmployeeCommandHandler(): CommandHandler {
    return AddEmployeeCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun createStoreHouseCommandHandler(): CommandHandler {
    return CreateStoreHouseCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun storeHouseCommandHandler(): CommandHandler {
    return StoreHouseCommandHandler(
      telegramBot,
      connectorConfiguration.storeHouseConnector()
    )
  }

  @Bean
  open fun languageCommandHandler(): CommandHandler {
    return LanguageCommandHandler(
      telegramBot,
      connectorConfiguration.telegramClientConnector(),
      serviceConfiguration.telegramClientCache(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun employeeCommandHandler(): CommandHandler {
    return EmployeeCommandHandler(
      telegramBot,
      connectorConfiguration.telegramClientConnector(),
      connectorConfiguration.workerTelegramConnector(),
      serviceConfiguration.keyboardBuilder(),
      serviceConfiguration.telegramClientCache(),
    )
  }
}
