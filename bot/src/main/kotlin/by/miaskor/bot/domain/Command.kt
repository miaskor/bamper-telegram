package by.miaskor.bot.domain

import by.miaskor.bot.service.enrich.FieldEnrich

enum class Command(private vararg val commands: String) {

  @FieldEnrich("changeLanguages", "commands")
  CHANGE_LANGUAGE,

  @FieldEnrich("languages", "commands")
  LANGUAGE,

  @FieldEnrich("employee", "commands")
  EMPLOYEE,

  @FieldEnrich("employees", "commands")
  EMPLOYEES,

  @FieldEnrich("addEmployees", "commands")
  ADD_EMPLOYEE,

  @FieldEnrich("removeEmployees", "commands")
  REMOVE_EMPLOYEE,

  @FieldEnrich("listEmployees", "commands")
  LIST_EMPLOYEE,

  @FieldEnrich("storeHouse", "commands")
  STORE_HOUSE,

  @FieldEnrich("createStoreHouses", "commands")
  CREATE_STORE_HOUSE,

  @FieldEnrich("chooseStoreHouses", "commands")
  CHOOSE_STORE_HOUSE,

  @FieldEnrich("storeHouse", "commands")
  SELECT_CERTAIN_STORE_HOUSE,

  @FieldEnrich("createAutoParts", "commands")
  CREATE_AUTO_PART,

  @FieldEnrich("createCars", "commands")
  CREATE_CAR,

  @FieldEnrich("backs", "commands")
  BACK,

  @FieldEnrich("nextSteps", "commands")
  NEXT_STEP,

  @FieldEnrich("previousSteps", "commands")
  PREVIOUS_STEP,

  @FieldEnrich("listCars", "commands")
  LIST_CAR,

  @FieldEnrich("undefined", "commands")
  UNDEFINED;

  infix fun isCommand(command: String): Boolean {
    return this.commands.firstOrNull { command.matches(Regex(it)) } != null
  }
}
