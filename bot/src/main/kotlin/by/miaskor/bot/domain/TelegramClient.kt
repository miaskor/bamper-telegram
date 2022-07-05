package by.miaskor.bot.domain

import by.miaskor.bot.domain.BotState.GREETINGS
import by.miaskor.bot.domain.BotState.MAIN_MENU
import java.util.*

data class TelegramClient(
  val chatId: Long,
  val chatLanguage: String = "EN",
  val bamperSessionId: String = "",
  val currentBotState: BotState = GREETINGS,
  val previousBotStates: SortedSet<BotState> = sortedSetOf(MAIN_MENU),
  private var employees: TelegramClientEmployees = TelegramClientEmployees(),
  private var telegramClientStoreHouses: TelegramClientStoreHouses = TelegramClientStoreHouses(),
) {

  fun isAuth(): Boolean {
    return bamperSessionId.isNotEmpty()
  }

  fun currentStoreHouseName(): String {
    return telegramClientStoreHouses.currentStoreHouse.name
  }

  fun storeHouses(): Set<StoreHouse> {
    return setOf<StoreHouse>().plus(telegramClientStoreHouses.storeHouses)
  }

  fun currentStoreHouseId(): Long {
    return telegramClientStoreHouses.currentStoreHouse.id
  }

  fun isModifiableStoreHouse(): Boolean {
    return telegramClientStoreHouses.currentStoreHouse.modifiable
  }

  fun refreshStoreHouses(storeHouses: Set<StoreHouse>) {
    telegramClientStoreHouses = telegramClientStoreHouses.copy(
      isModified = false,
      storeHouses = setOf<StoreHouse>().plus(storeHouses),
    )
  }

  fun refreshCurrentStoreHouse(currentStoreHouse: StoreHouse) {
    telegramClientStoreHouses = telegramClientStoreHouses.copy(
      isModified = false,
      storeHouses = telegramClientStoreHouses.storeHouses.plus(currentStoreHouse),
      currentStoreHouse = currentStoreHouse
    )
  }

  fun isEmployeesModified(): Boolean {
    return employees.isModified
  }

  fun getEmployees(): Set<Pair<String, Long>> {
    return employees.employees
  }

  fun addEmployee(employee: Pair<String, Long>) {
    employees = employees.copy(
      employees = getEmployees().plus(employee),
      isModified = true
    )
  }

  fun removeEmployee(employee: Pair<String, Long>) {
    employees = employees.copy(
      employees = getEmployees().minus(employee),
      isModified = true
    )
  }
}
