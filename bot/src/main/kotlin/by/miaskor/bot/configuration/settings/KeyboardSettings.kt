package by.miaskor.bot.configuration.settings

interface KeyboardSettings {
  fun choosingLanguageMenu(): Array<Array<String>>
  fun changingLanguageMenu(): Array<Array<String>>
  fun mainMenu(): Array<Array<String>>
  fun employeeMenu(): Array<Array<String>>
  fun addingEmployee(): Array<Array<String>>
  fun removingEmployee(): Array<Array<String>>
  fun storeHouseMenu(): Array<Array<String>>
}
