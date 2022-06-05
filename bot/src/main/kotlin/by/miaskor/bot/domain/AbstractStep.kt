package by.miaskor.bot.domain

interface AbstractStep<out T : Enum<out T>> {
  fun next(): T
  fun previous(): T

  fun isFinalStep(): Boolean

  fun isStepNotMandatory(): Boolean
}
