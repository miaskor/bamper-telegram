package by.miaskor.bot.configuration.settings

interface KeyboardSettings {
  fun chooseLanguageMenu(): Array<Array<String>>
  fun mainMenu(): Array<Array<String>>
  fun employeeMenu(): Array<Array<String>>
  fun addingEmployee(): Array<Array<String>>
  fun storeHouseMenu(): Array<Array<String>>
}
