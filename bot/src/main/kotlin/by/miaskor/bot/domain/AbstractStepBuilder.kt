package by.miaskor.bot.domain

abstract class AbstractStepBuilder<T> where T : Enum<T>, T : AbstractStep<T> {

  abstract var step: T

  fun nextStep(): AbstractStepBuilder<T> {
    this.step = step.next()
    return this
  }

  fun currentStep(): T {
    return step
  }

  fun previousStep(): AbstractStepBuilder<T> {
    this.step = step.previous()
    return this
  }
}