package by.miaskor.bot.domain

import java.util.concurrent.ConcurrentHashMap

data class TelegramClientStoreHouses(
  val isModified: Boolean = true,
  var currentStoreHouse: Pair<String, Long> = Pair("", -1),
  val storeHouses: Map<String, Long> = ConcurrentHashMap()
)
