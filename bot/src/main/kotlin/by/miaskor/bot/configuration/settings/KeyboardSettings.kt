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
  fun modificationStoreHouseMenu(): List<String>
  fun readStoreHouseMenu(): List<String>
  fun creatingAutoPartMenu(): List<String>
  fun creatingCarMenu(): List<String>
  fun addingEmployeeToStoreHouseMenu(): List<String>
  fun keyboardForMandatorySteps(): List<String>
  fun keyboardForNotMandatorySteps(): List<String>
  fun keyboardForLists(): List<String>
  fun deletingCarMenu(): List<String>
  fun deletingAutoPartMenu(): List<String>
}
