package by.miaskor.bot.domain

data class ListEntity(
  val carOffset: Long,
  val autoPartOffset: Long,
  var reachLimitCars: Boolean = false,
  var reachLimitAutoParts: Boolean = false
)
