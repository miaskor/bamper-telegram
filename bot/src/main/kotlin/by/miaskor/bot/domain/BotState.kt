package by.miaskor.bot.domain

import by.miaskor.bot.domain.Command.CHANGE_LANGUAGE
import by.miaskor.bot.domain.Command.EMPLOYEES

enum class BotState(vararg val commands: Command) {
  GREETINGS,
  CHOOSE_LANGUAGE,
  MAIN_MENU(CHANGE_LANGUAGE, EMPLOYEES),
  EMPLOYEES_MENU,
  STORE_HOUSES_MENU,
  STORE_HOUSE_MENU,
  FINDING_PARTS_MENU,
  BAMPER_MENU;

  companion object {
    private val mapOrdinals = values().drop(2).associateBy { it.ordinal }
  }

  fun getCommand(command: String): Command {
    return this.commands
      .first { it.isCommand(command) }
  }

  fun previous(): BotState {
    val currentState = this.ordinal
    val nextState = currentState - 1
    return mapOrdinals[nextState] ?: MAIN_MENU
  }
}
