package by.miaskor.bot.domain

import by.miaskor.bot.domain.BotState.GREETINGS
import by.miaskor.bot.domain.BotState.MAIN_MENU
import java.util.*

data class TelegramClient(
  val chatId: Long,
  val chatLanguage: String = "EN",
  val bamperClientId: Long? = null,
  val currentBotState: BotState = GREETINGS,
  val previousBotStates: SortedSet<BotState> = sortedSetOf(MAIN_MENU),
  var employeeUsernames: TelegramClientEmployees = TelegramClientEmployees(),
  var telegramClientStoreHouses: TelegramClientStoreHouses = TelegramClientStoreHouses()
) {

  fun refreshStoreHouses(storeHouseNames: List<String>) {
    telegramClientStoreHouses = telegramClientStoreHouses.copy(
      isModified = false,
      storeHouses = sortedSetOf<String>().plus(storeHouseNames),
    )
  }

  fun refreshCurrentStoreHouse(currentStoreHouse: String) {
    telegramClientStoreHouses = telegramClientStoreHouses.copy(
      isModified = false,
      storeHouses = sortedSetOf<String>().plus(telegramClientStoreHouses.storeHouses),
      currentStoreHouse = currentStoreHouse
    )
  }

  fun modifiedEmployees() {
    employeeUsernames = employeeUsernames.copy(
      isModified = true,
      employees = employeeUsernames.employees
    )
  }

  fun refreshEmployees(usernames: List<String>) {
    employeeUsernames = employeeUsernames.copy(
      isModified = false,
      employees = sortedSetOf<String>().plus(usernames)
    )
  }
}
