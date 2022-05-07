package by.miaskor.bot.domain

enum class BotState {
  GREETINGS,
  CHOOSE_LANGUAGE,
  MAIN_MENU,
  EMPLOYEES_MENU,
  STORE_HOUSES_MENU,
  STORE_HOUSE_MENU,
  FINDING_PARTS_MENU,
  BAMPER_MENU;

  companion object {
    private val mapOrdinals = values().drop(2).associateBy { it.ordinal }
  }

  fun previous(): BotState {
    val currentState = this.ordinal
    val nextState = currentState - 1
    return mapOrdinals[nextState] ?: MAIN_MENU
  }
}