package by.miaskor.bot.configuration.settings

interface CommandSettings {
  fun changeLanguageMessage(): String
  fun employeeUsernameMessage(): String
  fun undefinedCommandMessage(): String
  fun chooseLanguageFailMessage(): String
  fun incorrectUsernameFormatMessage(): String
  fun incorrectStoreHouseNameFormatMessage(): String
}
