package by.miaskor.bot.domain

data class TelegramClientStoreHouses(
  val isModified: Boolean = true,
  var currentStoreHouse: String = "",
  val storeHouses: Set<String> = sortedSetOf()
)
