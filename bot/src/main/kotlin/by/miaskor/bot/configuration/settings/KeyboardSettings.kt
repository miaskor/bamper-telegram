package by.miaskor.bot.configuration.settings

interface KeyboardSettings {
  fun chooseLanguageMenuFirstRow(): Array<String>
  fun mainMenuFirstRow(): Array<String>
  fun mainMenuSecondRow(): Array<String>
  fun mainMenuThirdRow(): Array<String>/*
  fun employeesMenuFirstRow(): Array<String>
  fun storeHouseFirstRow(): Array<String>
  fun storeHouseSecondRow(): Array<String>
  fun findingPartsMenuFirstRow(): Array<String>
  fun findingPartsMenuSecondRow(): Array<String>*/
}
