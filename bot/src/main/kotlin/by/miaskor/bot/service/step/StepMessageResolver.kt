package by.miaskor.bot.service.step

import by.miaskor.bot.domain.AbstractStep
import by.miaskor.bot.domain.AbstractStepBuilder
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

interface StepMessageResolver<T> where T : Enum<T>, T : AbstractStep<T> {

  fun resolve(
    update: Update,
    step: T,
    stepBuilder: AbstractStepBuilder<T>,
    isValid: Boolean
  ): Mono<String>
}