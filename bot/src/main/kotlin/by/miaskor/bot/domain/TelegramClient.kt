package by.miaskor.bot.domain

import by.miaskor.bot.domain.BotState.GREETINGS
import by.miaskor.bot.domain.BotState.MAIN_MENU
import java.util.*
import java.util.concurrent.ConcurrentHashMap

data class TelegramClient(
  val chatId: Long,
  val chatLanguage: String = "EN",
  val bamperClientId: Long? = null,
  val currentBotState: BotState = GREETINGS,
  val previousBotStates: SortedSet<BotState> = sortedSetOf(MAIN_MENU),
  var employeeUsernames: TelegramClientEmployees = TelegramClientEmployees(),
  var telegramClientStoreHouses: TelegramClientStoreHouses = TelegramClientStoreHouses()
) {

  fun currentStoreHouseName(): String {
    return telegramClientStoreHouses.currentStoreHouse.first
  }

  fun currentStoreHouseId(): Long {
    return telegramClientStoreHouses.currentStoreHouse.second
  }

  fun refreshStoreHouses(mapStoreHouses: Map<String, Long>) {
    telegramClientStoreHouses = telegramClientStoreHouses.copy(
      isModified = false,
      storeHouses = ConcurrentHashMap(mapStoreHouses),
    )
  }

  fun refreshCurrentStoreHouse(currentStoreHouse: Pair<String, Long>) {
    telegramClientStoreHouses = telegramClientStoreHouses.copy(
      isModified = false,
      storeHouses = telegramClientStoreHouses.storeHouses.plus(currentStoreHouse),
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
