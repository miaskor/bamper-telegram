package by.miaskor.bot.configuration.settings

interface StateSettings {
  fun greetingsMessage(): String
  fun chooseLanguageFailMessage(): String
  fun mainMenuMessage(): String
  fun employeesMenuMessage(): String
}
