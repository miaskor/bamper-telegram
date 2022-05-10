package by.miaskor.bot.configuration.settings

interface StateSettings {
  fun greetingsMessage(): String
  fun mainMenuMessage(): String
  fun employeesMenuMessage(): String
  fun employeeMessage(): String
  fun employeeFoundFailMessage(): String
  fun addingEmployeeSuccessMessage(): String
  fun removingEmployeeSuccessMessage(): String
  fun employeeIsYourEmployerMessage(): String
  fun employeeIsYouMessage(): String
}
