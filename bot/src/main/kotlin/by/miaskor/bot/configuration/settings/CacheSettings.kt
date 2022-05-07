package by.miaskor.bot.configuration.settings

interface CacheSettings {
  fun size(): Long
  fun entityTtl(): String
}
