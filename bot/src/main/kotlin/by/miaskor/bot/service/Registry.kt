package by.miaskor.bot.service

class Registry<K : Any, V : Any>(
  private val mapObjects: Map<K, V>
) {

  fun lookup(key: K): V {
    return mapObjects[key]
      ?: throw IllegalArgumentException("Registry don't have object with key=$key")
  }
}