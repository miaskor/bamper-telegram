package by.miaskor.bot.domain

data class TelegramClientEmployees(
  val isModified: Boolean = true,
  val employees: Set<Pair<String, Long>> = sortedSetOf(),
)
