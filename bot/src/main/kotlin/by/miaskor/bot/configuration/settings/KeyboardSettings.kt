package by.miaskor.bot.configuration.settings

interface KeyboardSettings {
  fun choosingLanguageMenu(): List<String>
  fun changingLanguageMenu(): List<String>
  fun mainMenu(): List<String>
  fun employeeMenu(): List<String>
  fun modificationStoreHouseMenu(): List<String>
  fun readStoreHouseMenu(): List<String>
  fun keyboardForMandatorySteps(): List<String>
  fun keyboardForNotMandatorySteps(): List<String>
  fun keyboardForLists(): List<String>
  fun findAutoPartMenu(): List<String>
  fun defaultMenu(): List<String>
  fun bamperMenu(): List<String>
}
