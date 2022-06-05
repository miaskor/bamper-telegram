package by.miaskor.bot.service.step

import by.miaskor.bot.domain.AbstractStep
import by.miaskor.bot.domain.AbstractStepBuilder
import com.pengrad.telegrambot.model.Update

interface StepBuilderFieldEnricher<T> where T : Enum<T>, T : AbstractStep<T> {

  fun enrich(update: Update, step: T, stepBuilder: AbstractStepBuilder<T>)
}