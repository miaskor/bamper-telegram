package by.miaskor.bot.domain

import by.miaskor.bot.domain.Command.ADD_EMPLOYEE
import by.miaskor.bot.domain.Command.BACK
import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.domain.Command.EMPLOYEES
import by.miaskor.bot.domain.Command.LIST_EMPLOYEE
import reactor.core.publisher.Mono

enum class BotState(private vararg val commands: Command) {
  GREETINGS,
  CHOOSE_LANGUAGE(BACK),
  ADDING_EMPLOYEE(BACK),
  MAIN_MENU(CHANGE_LANGUAGE, EMPLOYEES),
  EMPLOYEES_MENU(LIST_EMPLOYEE, ADD_EMPLOYEE, BACK),
  STORE_HOUSES_MENU,
  STORE_HOUSE_MENU,
  FINDING_PARTS_MENU,
  BAMPER_MENU;

  companion object {
    private val finalStates = listOf(ADDING_EMPLOYEE, MAIN_MENU, CHOOSE_LANGUAGE)

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
