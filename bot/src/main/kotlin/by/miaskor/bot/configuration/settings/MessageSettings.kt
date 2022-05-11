package by.miaskor.bot.configuration.settings

interface MessageSettings {
  fun changeLanguageMessage(): String
  fun employeeMessage(): String
  fun undefinedCommandMessage(): String
  fun chooseLanguageFailMessage(): String
  fun incorrectUsernameFormatMessage(): String
  fun inputEmployeeMessage(): String
  fun failFoundEmployeeMessage(): String
  fun incorrectStoreHouseNameFormatMessage(): String
  fun greetingsMessage(): String
  fun mainMenuMessage(): String
  fun employeesMenuMessage(): String
  fun addingEmployeeSuccessMessage(): String
  fun removingEmployeeSuccessMessage(): String
  fun employeeIsYourEmployerMessage(): String
  fun employeeIsYouMessage(): String
  fun storeHouseMessage(): String
  fun addingStoreHouseFailMessage(): String
  fun addingStoreHouseSuccessMessage(): String
}
