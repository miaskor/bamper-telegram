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
  fun storeHouseMenuMessage(): String
  fun addingEmployeeSuccessMessage(): String
  fun removingEmployeeSuccessMessage(): String
  fun employeeIsYourEmployerMessage(): String
  fun employeeIsYouMessage(): String
  fun storeHouseMessage(): String
  fun creatingAutoPartMessage(): String
  fun creatingCarMessage(): String
  fun addingStoreHouseFailMessage(): String
  fun addingStoreHouseSuccessMessage(): String
  fun allStoreHousesMessage(): String
  fun listCarMessage(): String
  fun storeHouseNotFoundMessage(): String
}
