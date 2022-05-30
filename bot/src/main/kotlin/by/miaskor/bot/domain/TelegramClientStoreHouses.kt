package by.miaskor.bot.domain

data class TelegramClientStoreHouses(
  val isModified: Boolean = true,
  var currentStoreHouse: StoreHouse = StoreHouse("", -1),
  val storeHouses: Set<StoreHouse> = setOf()
)
