package by.miaskor.bot.service.step

import by.miaskor.bot.domain.AbstractStep
import by.miaskor.bot.domain.AbstractStepBuilder
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

interface StepValidator<T> where T : Enum<T>, T : AbstractStep<T> {

  fun validate(step: T, update: Update, stepBuilder: AbstractStepBuilder<T>): Mono<Boolean>
}