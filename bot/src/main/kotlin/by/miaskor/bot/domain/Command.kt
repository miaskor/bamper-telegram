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

  @FieldEnrich("listAutoParts", "commands")
  LIST_AUTO_PART,

  @FieldEnrich("findAutoPartByPartNumbers", "commands")
  FIND_AUTO_PART_BY_PART_NUMBER,

  FIND_AUTO_PART_BY_PART_NUMBER_ENTITY(".*"),

  @FieldEnrich("findAutoPartByIds", "commands")
  FIND_AUTO_PART_BY_PART_ID,

  @FieldEnrich("findAutoPartByCarAndCarParts", "commands")
  FIND_AUTO_PART_BY_CAR_AND_CAR_PART,

  @FieldEnrich("autoPartByCarAndCarPart", "commands")
  FIND_AUTO_PART_BY_CAR_AND_CAR_PART_ENTITY,

  @FieldEnrich("entityIds", "commands")
  FIND_AUTO_PART_BY_PART_ID_ENTITY,

  @FieldEnrich("addEmployeeToStoreHouses", "commands")
  ADD_EMPLOYEE_TO_STORE_HOUSE,

  @FieldEnrich("employeeToStoreHouse", "commands")
  EMPLOYEE_TO_STORE_HOUSE,

  @FieldEnrich("deleteCars", "commands")
  DELETE_CARS,

  @FieldEnrich("deleteAutoParts", "commands")
  DELETE_AUTO_PARTS,

  @FieldEnrich("entityIds", "commands")
  DELETE_CAR,

  @FieldEnrich("entityIds", "commands")
  DELETE_AUTO_PART,

  @FieldEnrich("findAutoParts", "commands")
  FIND_AUTO_PART,

  @FieldEnrich("undefined", "commands")
  UNDEFINED;

  infix fun isCommand(command: String): Boolean {
    return this.commands.firstOrNull { command.matches(Regex(it)) } != null
  }
}
