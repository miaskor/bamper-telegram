package by.miaskor.bot.domain

enum class StateBot {
  CHOOSE_LANGUAGE,
  MAIN_MENU,
  EMPLOYEES_MENU,
  STORE_HOUSES_MENU,
  STORE_HOUSE_MENU,
  FOUNDING_PARTS_MENU,
  BAMPER_MENU;

  companion object{
    private val mapOrdinals = values().associateBy { it.ordinal }
  }
  fun next(): StateBot {
    val currentState = this.ordinal
    val nextState = currentState + 1
    return mapOrdinals[nextState] ?: BAMPER_MENU
  }
}
