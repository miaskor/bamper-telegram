package by.miaskor.bot.domain

interface AbstractStep<T : Enum<T>> {
  fun next(): T
  fun previous(): T
}
