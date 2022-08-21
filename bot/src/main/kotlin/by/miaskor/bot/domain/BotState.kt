package by.miaskor.bot.domain

import by.miaskor.bot.domain.Command.ADD_EMPLOYEE
import by.miaskor.bot.domain.Command.ADD_EMPLOYEE_TO_STORE_HOUSE
import by.miaskor.bot.domain.Command.AUTH_BAMPER
import by.miaskor.bot.domain.Command.BACK
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.domain.Command.CHOOSE_STORE_HOUSE
import by.miaskor.bot.domain.Command.CREATE_AUTO_PART
import by.miaskor.bot.domain.Command.CREATE_CAR
import by.miaskor.bot.domain.Command.CREATE_STORE_HOUSE
import by.miaskor.bot.domain.Command.DELETE_AUTO_PART
import by.miaskor.bot.domain.Command.DELETE_AUTO_PARTS
import by.miaskor.bot.domain.Command.DELETE_CAR
import by.miaskor.bot.domain.Command.DELETE_CARS
import by.miaskor.bot.domain.Command.EMPLOYEE
import by.miaskor.bot.domain.Command.EMPLOYEES
import by.miaskor.bot.domain.Command.EMPLOYEE_TO_STORE_HOUSE
import by.miaskor.bot.domain.Command.FIND_AUTO_PART
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_CAR_AND_CAR_PART
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_CAR_AND_CAR_PART_ENTITY
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_PART_ID
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_PART_ID_ENTITY
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_PART_NUMBER
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_PART_NUMBER_ENTITY
import by.miaskor.bot.domain.Command.IMPORT_AUTO_PART
import by.miaskor.bot.domain.Command.IMPORT_AUTO_PART_BY_ID
import by.miaskor.bot.domain.Command.LANGUAGE
import by.miaskor.bot.domain.Command.LIST_AUTO_PART
import by.miaskor.bot.domain.Command.LIST_CAR
import by.miaskor.bot.domain.Command.LIST_EMPLOYEE
import by.miaskor.bot.domain.Command.LOG_IN_BAMPER
import by.miaskor.bot.domain.Command.LOG_OUT_BAMPER
import by.miaskor.bot.domain.Command.REMOVE_EMPLOYEE
import by.miaskor.bot.domain.Command.SELECT_CERTAIN_STORE_HOUSE
import by.miaskor.bot.domain.Command.STORE_HOUSE
import by.miaskor.bot.domain.Command.UNDEFINED
import reactor.core.publisher.Mono

enum class BotState(private vararg val commands: Command) {
  GREETINGS,
  CHOOSING_LANGUAGE(LANGUAGE, UNDEFINED),
  CHANGING_LANGUAGE(LANGUAGE, BACK, UNDEFINED),
  ADDING_EMPLOYEE(BACK, EMPLOYEE, UNDEFINED),
  REMOVING_EMPLOYEE(BACK, EMPLOYEE, UNDEFINED),
  MAIN_MENU(CREATE_STORE_HOUSE, CHOOSE_STORE_HOUSE, CHANGE_LANGUAGE, LOG_IN_BAMPER, EMPLOYEES),
  CREATING_STORE_HOUSE(BACK, STORE_HOUSE, UNDEFINED),
  CHOOSING_STORE_HOUSE(BACK, SELECT_CERTAIN_STORE_HOUSE, UNDEFINED),
  MODIFICATION_STORE_HOUSE_MENU(
    BACK, CREATE_AUTO_PART, FIND_AUTO_PART, CREATE_CAR,
    LIST_CAR, LIST_AUTO_PART,
    DELETE_CARS, DELETE_AUTO_PARTS,
    ADD_EMPLOYEE_TO_STORE_HOUSE, UNDEFINED
  ),
  READ_ONLY_STORE_HOUSE_MENU(
    BACK, LIST_CAR, FIND_AUTO_PART, LIST_AUTO_PART, UNDEFINED
  ),
  EMPLOYEES_MENU(LIST_EMPLOYEE, ADD_EMPLOYEE, REMOVE_EMPLOYEE, BACK, UNDEFINED),
  ADDING_EMPLOYEE_TO_STORE_HOUSE(EMPLOYEE_TO_STORE_HOUSE, BACK, UNDEFINED),
  CREATING_AUTO_PART(BACK),
  DELETING_AUTO_PART(DELETE_AUTO_PART, BACK, UNDEFINED),
  CREATING_CAR(BACK),
  DELETING_CAR(DELETE_CAR, BACK, UNDEFINED),
  FINDING_AUTO_PART(
    FIND_AUTO_PART_BY_PART_ID, FIND_AUTO_PART_BY_PART_NUMBER,
    FIND_AUTO_PART_BY_CAR_AND_CAR_PART, BACK, UNDEFINED
  ),
  FINDING_AUTO_PART_BY_PART_NUMBER(BACK, FIND_AUTO_PART_BY_PART_NUMBER_ENTITY),
  FINDING_AUTO_PART_BY_ID(BACK, FIND_AUTO_PART_BY_PART_ID_ENTITY, UNDEFINED),
  FINDING_AUTO_PART_BY_CAR_AND_CAR_PART(BACK, FIND_AUTO_PART_BY_CAR_AND_CAR_PART_ENTITY, UNDEFINED),
  AUTHORIZATION_BAMPER(BACK, AUTH_BAMPER, UNDEFINED),
  BAMPER_MENU(BACK, IMPORT_AUTO_PART, LOG_OUT_BAMPER, UNDEFINED),
  IMPORTING_AUTO_PART_BY_ID(BACK, IMPORT_AUTO_PART_BY_ID, UNDEFINED);

  companion object {
    private val finalStates =
      listOf(
        GREETINGS, ADDING_EMPLOYEE, REMOVING_EMPLOYEE, MAIN_MENU, CHOOSING_LANGUAGE,
        CHANGING_LANGUAGE, CREATING_STORE_HOUSE, CREATING_CAR, CREATING_AUTO_PART,
        ADDING_EMPLOYEE_TO_STORE_HOUSE, DELETING_CAR, DELETING_AUTO_PART, FINDING_AUTO_PART_BY_PART_NUMBER,
        FINDING_AUTO_PART_BY_ID, FINDING_AUTO_PART_BY_CAR_AND_CAR_PART, AUTHORIZATION_BAMPER
      )

    fun isNotFinalState(botState: BotState): Boolean {
      return !finalStates.contains(botState)
    }
  }

  fun getCommand(command: String): Mono<Command> {
    return Mono.fromSupplier {
      this.commands
        .firstOrNull { it isCommand command }
    }
  }
}
