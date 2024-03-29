package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE_TO_STORE_HOUSE
import by.miaskor.bot.domain.BotState.CHANGING_LANGUAGE
import by.miaskor.bot.domain.BotState.CREATING_AUTO_PART
import by.miaskor.bot.domain.BotState.CREATING_CAR
import by.miaskor.bot.domain.BotState.CREATING_STORE_HOUSE
import by.miaskor.bot.domain.BotState.DELETING_AUTO_PART
import by.miaskor.bot.domain.BotState.DELETING_CAR
import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART_BY_CAR_AND_CAR_PART
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART_BY_ID
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART_BY_PART_NUMBER
import by.miaskor.bot.domain.BotState.IMPORTING_AUTO_PART_BY_ID
import by.miaskor.bot.domain.BotState.REMOVING_EMPLOYEE
import by.miaskor.bot.domain.Command.ADD_EMPLOYEE
import by.miaskor.bot.domain.Command.ADD_EMPLOYEE_TO_STORE_HOUSE
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.domain.Command.CREATE_AUTO_PART
import by.miaskor.bot.domain.Command.CREATE_CAR
import by.miaskor.bot.domain.Command.CREATE_STORE_HOUSE
import by.miaskor.bot.domain.Command.DELETE_AUTO_PARTS
import by.miaskor.bot.domain.Command.DELETE_CARS
import by.miaskor.bot.domain.Command.EMPLOYEES
import by.miaskor.bot.domain.Command.FIND_AUTO_PART
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_CAR_AND_CAR_PART
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_PART_ID
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_PART_NUMBER
import by.miaskor.bot.domain.Command.IMPORT_AUTO_PART
import by.miaskor.bot.domain.Command.REMOVE_EMPLOYEE
import by.miaskor.bot.domain.ConstraintAutoPartListEntity
import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.service.CallBackCommandHandler
import by.miaskor.bot.service.handler.command.BackCommandHandler
import by.miaskor.bot.service.handler.command.ChangingBotStateCommandHandler
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.command.UndefinedCommandHandler
import by.miaskor.bot.service.handler.command.autopart.DeleteAutoPartCommandHandler
import by.miaskor.bot.service.handler.command.autopart.FindAutoPartByCarAndCarPartCommandHandler
import by.miaskor.bot.service.handler.command.autopart.FindAutoPartByIdCommandHandler
import by.miaskor.bot.service.handler.command.autopart.FindAutoPartByPartNumberCommandHandler
import by.miaskor.bot.service.handler.command.autopart.ListAutoPartCommandHandler
import by.miaskor.bot.service.handler.command.bamper.AuthBamperCommandHandler
import by.miaskor.bot.service.handler.command.bamper.ImportAutoPartByIdCommandHandler
import by.miaskor.bot.service.handler.command.bamper.LogInBamperCommandHandler
import by.miaskor.bot.service.handler.command.bamper.LogOutBamperCommandHandler
import by.miaskor.bot.service.handler.command.car.DeleteCarCommandHandler
import by.miaskor.bot.service.handler.command.car.ListCarCommandHandler
import by.miaskor.bot.service.handler.command.employee.AddingEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.employee.AddingEmployeeToStoreHouseCommandHandler
import by.miaskor.bot.service.handler.command.employee.EmployeeCommandHandler
import by.miaskor.bot.service.handler.command.employee.ListEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.employee.RemovingEmployeeCommandHandler
import by.miaskor.bot.service.handler.command.language.LanguageCommandHandler
import by.miaskor.bot.service.handler.command.storehouse.ChooseStoreHouseCommandHandler
import by.miaskor.bot.service.handler.command.storehouse.SelectCertainStoreHouseCommandHandler
import by.miaskor.bot.service.handler.command.storehouse.StoreHouseCommandHandler
import by.miaskor.bot.service.handler.list.ListAutoPartHandler
import by.miaskor.bot.service.handler.list.ListCarHandler
import by.miaskor.bot.service.handler.list.ListEntityHandler
import by.miaskor.bot.service.handler.list.ListEntityHandlerRegistry
import by.miaskor.bot.service.handler.list.ListFindAutoPartHandler
import by.miaskor.bot.service.handler.list.ListHandler
import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CommandHandlerConfiguration(
  private val telegramBot: TelegramBot,
  private val serviceConfiguration: ServiceConfiguration,
  private val cacheConfiguration: CacheConfiguration,
  private val settingsConfiguration: SettingsConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

  @Bean
  open fun listEntityHandler(): ListEntityHandler {
    return ListEntityHandler(
      cacheConfiguration.listEntityCacheRegistry(),
      listEntityHandlerRegistry()
    )
  }

  @Bean
  open fun callBackCommandHandler(): CallBackCommandHandler {
    return CallBackCommandHandler(listEntityHandler())
  }

  @Bean
  open fun commandHandlerRegistry(): CommandHandlerRegistry {
    return CommandHandlerRegistry(
      addEmployeeToStoreHouseCommandHandler(),
      changeLanguageCommandHandler(),
      employeesCommandHandler(),
      backCommandHandler(),
      undefinedCommandHandler(),
      addEmployeeCommandHandler(),
      listEmployeeCommandHandler(),
      languageCommandHandler(),
      employeeCommandHandler(),
      addingEmployeeToStoreHouseCommandHandler(),
      removeEmployeeCommandHandler(),
      createStoreHouseCommandHandler(),
      storeHouseCommandHandler(),
      chooseStoreHouseCommandHandler(),
      selectCertainStoreHouseCommandHandler(),
      createAutoPartCommandHandler(),
      listCarCommandHandler(),
      deleteAutoPartCommandHandler(),
      deleteCarCommandHandler(),
      listAutoPartCommandHandler(),
      findAutoPartByPartNumberCommandHandler(),
      findAutoPartByCarAndCarPartCommandHandler(),
      findAutoPartByIdCommandHandler(),
      deleteCarsCommandHandler(),
      deleteAutoPartsCommandHandler(),
      findAutoPartsCommandHandler(),
      logInBamperCommandHandler(),
      logOutBamperCommandHandler(),
      authBamperCommandHandler(),
      findAutoPartsByPartNumberCommandHandler(),
      findAutoPartsByCarAndCarPartCommandHandler(),
      findAutoPartsByIdCommandHandler(),
      importAutoPartByIdCommandHandler(),
      importAutoPartCommandHandler(),
      createCarCommandHandler()
    )
  }

  @Bean
  @Suppress("UNCHECKED_CAST")
  open fun listEntityHandlerRegistry(): ListEntityHandlerRegistry {
    return ListEntityHandlerRegistry(
      listCarHandler(),
      listAutoPartHandler(),
      listFindAutoPartHandler() as ListHandler<ListEntity>
    )
  }

  @Bean
  open fun deleteAutoPartsCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      DELETE_AUTO_PARTS,
      DELETING_AUTO_PART,
      MessageSettings::deletingAutoPartMessage
    )
  }

  @Bean
  open fun findAutoPartsCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      FIND_AUTO_PART,
      FINDING_AUTO_PART,
      MessageSettings::findAutoPartMenuMessage
    )
  }

  @Bean
  open fun importAutoPartCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      IMPORT_AUTO_PART,
      IMPORTING_AUTO_PART_BY_ID,
      MessageSettings::importAutoPartMessage
    )
  }

  @Bean
  open fun logInBamperCommandHandler(): CommandHandler {
    return LogInBamperCommandHandler(
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun importAutoPartByIdCommandHandler(): CommandHandler {
    return ImportAutoPartByIdCommandHandler(
      cacheConfiguration.telegramClientCache(),
      connectorConfiguration.bamperIntegrationConnector()
    )
  }

  @Bean
  open fun logOutBamperCommandHandler(): CommandHandler {
    return LogOutBamperCommandHandler(
      cacheConfiguration.telegramClientCache(),
      backCommandHandler()
    )
  }

  @Bean
  open fun authBamperCommandHandler(): CommandHandler {
    return AuthBamperCommandHandler(
      connectorConfiguration.bamperIntegrationConnector(),
      connectorConfiguration.bamperClientConnector(),
      telegramBot,
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun findAutoPartsByPartNumberCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      FIND_AUTO_PART_BY_PART_NUMBER,
      FINDING_AUTO_PART_BY_PART_NUMBER,
      MessageSettings::findAutoPartByPartNumberMessage
    )
  }

  @Bean
  open fun findAutoPartsByIdCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      FIND_AUTO_PART_BY_PART_ID,
      FINDING_AUTO_PART_BY_ID,
      MessageSettings::findAutoPartByIdMessage
    )
  }

  @Bean
  open fun findAutoPartsByCarAndCarPartCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      FIND_AUTO_PART_BY_CAR_AND_CAR_PART,
      FINDING_AUTO_PART_BY_CAR_AND_CAR_PART,
      MessageSettings::findAutoPartByCarAndCarPartMessage
    )
  }

  @Bean
  open fun deleteCarsCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      DELETE_CARS,
      DELETING_CAR,
      MessageSettings::deletingCarMessage
    )
  }

  @Bean
  open fun addEmployeeToStoreHouseCommandHandler(): CommandHandler {
    return ChangingBotStateCommandHandler(
      telegramBot,
      serviceConfiguration.keyboardBuilder(),
      ADD_EMPLOYEE_TO_STORE_HOUSE,
      ADDING_EMPLOYEE_TO_STORE_HOUSE,
      MessageSettings::inputEmployeeToStoreHouseMessage
    )
  }

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
      cacheConfiguration.telegramClientCache()
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
      connectorConfiguration.telegramClientConnector(),
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun listCarCommandHandler(): CommandHandler {
    return ListCarCommandHandler(
      listEntityHandler(),
      cacheConfiguration.carListEntityCache()
    )
  }

  @Bean
  open fun deleteCarCommandHandler(): CommandHandler {
    return DeleteCarCommandHandler(
      connectorConfiguration.carConnector(),
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun deleteAutoPartCommandHandler(): CommandHandler {
    return DeleteAutoPartCommandHandler(
      connectorConfiguration.autoPartConnector(),
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun listAutoPartCommandHandler(): CommandHandler {
    return ListAutoPartCommandHandler(
      listEntityHandler(),
      cacheConfiguration.autoPartListEntityCache()
    )
  }

  @Bean
  open fun findAutoPartByPartNumberCommandHandler(): CommandHandler {
    return FindAutoPartByPartNumberCommandHandler(
      listEntityHandler(),
      cacheConfiguration.autoPartByPartNumberListCache()
    )
  }

  @Bean
  open fun findAutoPartByCarAndCarPartCommandHandler(): CommandHandler {
    return FindAutoPartByCarAndCarPartCommandHandler(
      listEntityHandler(),
      cacheConfiguration.autoPartByCarAndCarPartListCache()
    )
  }

  @Bean
  open fun findAutoPartByIdCommandHandler(): CommandHandler {
    return FindAutoPartByIdCommandHandler(
      connectorConfiguration.autoPartConnector(),
      cacheConfiguration.telegramClientCache()
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
      cacheConfiguration.telegramClientCache(),
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
      connectorConfiguration.storeHouseConnector(),
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun selectCertainStoreHouseCommandHandler(): CommandHandler {
    return SelectCertainStoreHouseCommandHandler(
      serviceConfiguration.keyboardBuilder(),
      cacheConfiguration.telegramClientCache(),
      telegramBot
    )
  }

  @Bean
  open fun storeHouseCommandHandler(): CommandHandler {
    return StoreHouseCommandHandler(connectorConfiguration.storeHouseConnector())
  }

  @Bean
  open fun languageCommandHandler(): CommandHandler {
    return LanguageCommandHandler(
      connectorConfiguration.telegramClientConnector(),
      cacheConfiguration.telegramClientCache(),
    )
  }

  @Bean
  open fun employeeCommandHandler(): CommandHandler {
    return EmployeeCommandHandler(
      removingEmployeeCommandHandler(),
      addingEmployeeCommandHandler(),
      cacheConfiguration.telegramClientCache(),
    )
  }

  @Bean
  open fun addingEmployeeToStoreHouseCommandHandler(): CommandHandler {
    return AddingEmployeeToStoreHouseCommandHandler(
      connectorConfiguration.telegramClientConnector(),
      cacheConfiguration.telegramClientCache(),
      connectorConfiguration.workerStoreHouseConnector(),
      connectorConfiguration.workerTelegramConnector()
    )
  }

  @Bean
  open fun addingEmployeeCommandHandler(): AddingEmployeeCommandHandler {
    return AddingEmployeeCommandHandler(
      connectorConfiguration.telegramClientConnector(),
      connectorConfiguration.workerTelegramConnector(),
      cacheConfiguration.telegramClientCache(),
    )
  }

  @Bean
  open fun removingEmployeeCommandHandler(): RemovingEmployeeCommandHandler {
    return RemovingEmployeeCommandHandler(
      connectorConfiguration.telegramClientConnector(),
      connectorConfiguration.workerTelegramConnector(),
      cacheConfiguration.telegramClientCache(),
    )
  }

  @Bean
  open fun listCarHandler(): ListHandler<ListEntity> {
    return ListCarHandler(
      connectorConfiguration.carConnector(),
      settingsConfiguration.listSettings(),
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun listFindAutoPartHandler(): ListHandler<ConstraintAutoPartListEntity> {
    return ListFindAutoPartHandler(
      settingsConfiguration.listSettings(),
      connectorConfiguration.autoPartConnector(),
      cacheConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun listAutoPartHandler(): ListHandler<ListEntity> {
    return ListAutoPartHandler(
      settingsConfiguration.listSettings(),
      connectorConfiguration.autoPartConnector(),
      cacheConfiguration.telegramClientCache()
    )
  }
}
