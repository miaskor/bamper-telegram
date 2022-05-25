package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.domain.BotState.CHANGING_LANGUAGE
import by.miaskor.bot.domain.BotState.CREATING_AUTO_PART
import by.miaskor.bot.domain.BotState.CREATING_CAR
import by.miaskor.bot.domain.BotState.CREATING_STORE_HOUSE
import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.REMOVING_EMPLOYEE
import by.miaskor.bot.domain.Command.ADD_EMPLOYEE
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.domain.Command.CREATE_AUTO_PART
import by.miaskor.bot.domain.Command.CREATE_CAR
import by.miaskor.bot.domain.Command.CREATE_STORE_HOUSE
import by.miaskor.bot.domain.Command.EMPLOYEES
import by.miaskor.bot.domain.Command.REMOVE_EMPLOYEE
import by.miaskor.bot.service.handler.command.BackCommandHandler
import by.miaskor.bot.service.handler.command.ChangingBotStateCommandHandler
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.handler.command.UndefinedCommandHandler
import by.miaskor.bot.service.handler.command.car.ListCarCommandHandler
import by.miaskor.bot.service.handler.command.employee.EmployeeCommandHandler
import by.miaskor.bot.service.handler.command.employee.ListEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.language.LanguageCommandHandler
import by.miaskor.bot.service.handler.command.storehouse.ChooseStoreHouseCommandHandler
import by.miaskor.bot.service.handler.command.storehouse.SelectCertainStoreHouseCommandHandler
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
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      CHANGE_LANGUAGE,
      CHANGING_LANGUAGE,
      MessageSettings::changeLanguageMessage
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
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      EMPLOYEES,
      EMPLOYEES_MENU,
      MessageSettings::employeesMenuMessage
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
  open fun listCarCommandHandler(): CommandHandler {
    return ListCarCommandHandler(
      telegramBot,
      connectorConfiguration.carConnector(),
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun removeEmployeeCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      REMOVE_EMPLOYEE,
      REMOVING_EMPLOYEE,
      MessageSettings::inputEmployeeMessage
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
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      ADD_EMPLOYEE,
      ADDING_EMPLOYEE,
      MessageSettings::inputEmployeeMessage
    )
  }

  @Bean
  open fun createStoreHouseCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      CREATE_STORE_HOUSE,
      CREATING_STORE_HOUSE,
      MessageSettings::storeHouseMessage
    )
  }

  @Bean
  open fun createAutoPartCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      CREATE_AUTO_PART,
      CREATING_AUTO_PART,
      MessageSettings::creatingAutoPartMessage
    )
  }

  @Bean
  open fun createCarCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      CREATE_CAR,
      CREATING_CAR,
      MessageSettings::creatingCarMessage
    )
  }

  @Bean
  open fun chooseStoreHouseCommandHandler(): CommandHandler {
    return ChooseStoreHouseCommandHandler(
      serviceConfiguration.keyboardBuilder(),
      connectorConfiguration.storeHouseConnector(),
      telegramBot,
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun selectCertainStoreHouseCommandHandler(): CommandHandler {
    return SelectCertainStoreHouseCommandHandler(
      serviceConfiguration.keyboardBuilder(),
      serviceConfiguration.telegramClientCache(),
      telegramBot
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
