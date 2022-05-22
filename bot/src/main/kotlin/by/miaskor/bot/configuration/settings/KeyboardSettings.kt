package by.miaskor.bot.configuration.settings

interface KeyboardSettings {
  fun choosingLanguageMenu(): List<String>
  fun changingLanguageMenu(): List<String>
  fun mainMenu(): List<String>
  fun employeeMenu(): List<String>
  fun addingEmployee(): List<String>
  fun removingEmployee(): List<String>
  fun creatingStoreHouseMenu(): List<String>
  fun choosingStoreHouseMenu(): List<String>
  fun storeHouseMenu(): List<String>
  fun creatingAutoPartMenu(): List<String>
  fun creatingCarMenu(): List<String>
  fun keyboardForMandatorySteps(): List<String>
  fun keyboardForNotMandatorySteps(): List<String>
}
