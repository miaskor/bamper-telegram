package by.miaskor.bot.configuration.settings

interface StateSettings {
  fun greetingsMessage(): String
  fun chooseLanguageFailMessage(): String
  fun mainMenuMessage(): String
  fun employeesMenuMessage(): String
  fun addingEmployeeMessage(): String
  fun addingEmployeeFailMessage(): String
  fun addingEmployeeSuccessMessage(): String
  fun undefinedCommandMessage(): String
}
