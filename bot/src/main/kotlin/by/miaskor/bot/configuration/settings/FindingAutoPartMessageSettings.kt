package by.miaskor.bot.configuration.settings

interface FindingAutoPartMessageSettings {
  fun autoPartMessage(): String
  fun partNumberMessage(): String
  fun brandMessage(): String
  fun modelMessage(): String
  fun yearMessage(): String
  fun notFoundMessage(): String
}