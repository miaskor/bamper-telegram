package by.miaskor.bot.domain

import by.miaskor.bot.domain.Command.ADD_EMPLOYEE
import by.miaskor.bot.domain.Command.BACK
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.domain.Command.EMPLOYEE
import by.miaskor.bot.domain.Command.EMPLOYEES
import by.miaskor.bot.domain.Command.LANGUAGE
import by.miaskor.bot.domain.Command.LIST_EMPLOYEE
import by.miaskor.bot.domain.Command.REMOVE_EMPLOYEE
import by.miaskor.bot.domain.Command.UNDEFINED
import reactor.core.publisher.Mono

enum class BotState(private vararg val commands: Command) {
  GREETINGS,
  CHOOSING_LANGUAGE(LANGUAGE, UNDEFINED),
  CHANGING_LANGUAGE(LANGUAGE, BACK, UNDEFINED),
  ADDING_EMPLOYEE(BACK, EMPLOYEE, UNDEFINED),
  REMOVING_EMPLOYEE(BACK, EMPLOYEE, UNDEFINED),
  MAIN_MENU(CHANGE_LANGUAGE, EMPLOYEES),
  EMPLOYEES_MENU(LIST_EMPLOYEE, ADD_EMPLOYEE, REMOVE_EMPLOYEE, BACK),
  STORE_HOUSES_MENU,
  STORE_HOUSE_MENU,
  FINDING_PARTS_MENU,
  BAMPER_MENU;

  companion object {
    private val finalStates =
      listOf(
        ADDING_EMPLOYEE, REMOVING_EMPLOYEE, MAIN_MENU, CHOOSING_LANGUAGE,
        CHANGING_LANGUAGE
      )

    fun isNotFinalState(botState: BotState): Boolean {
      return !finalStates.contains(botState)
    }
  }

  fun getCommand(command: String): Mono<Command> {
    return Mono.fromSupplier {
      this.commands
        .firstOrNull { it.isCommand(command) }
    }
  }
}
