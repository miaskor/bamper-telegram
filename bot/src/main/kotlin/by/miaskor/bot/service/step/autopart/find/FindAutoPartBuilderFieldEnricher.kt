package by.miaskor.bot.service.step.autopart.find

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.FindAutoPartBuilder
import by.miaskor.bot.domain.FindingAutoPartStep
import by.miaskor.bot.domain.FindingAutoPartStep.AUTO_PART
import by.miaskor.bot.domain.FindingAutoPartStep.AUTO_PART_NUMBER
import by.miaskor.bot.domain.FindingAutoPartStep.BRAND
import by.miaskor.bot.domain.FindingAutoPartStep.CAR_YEAR
import by.miaskor.bot.domain.FindingAutoPartStep.MODEL
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.step.StepBuilderFieldEnricher
import com.pengrad.telegrambot.model.Update

class FindAutoPartBuilderFieldEnricher : StepBuilderFieldEnricher<FindingAutoPartStep> {
  override fun enrich(
    update: Update,
    step: FindingAutoPartStep,
    stepBuilder: AbstractStepBuilder<FindingAutoPartStep>
  ) {
    val findAutoPartBuilder = stepBuilder as FindAutoPartBuilder
    when (step) {
      AUTO_PART_NUMBER -> findAutoPartBuilder.partNumber(update.text)
      AUTO_PART -> findAutoPartBuilder.partNumber(update.text)
      BRAND -> findAutoPartBuilder.brand(update.text)
      MODEL -> findAutoPartBuilder.model(update.text)
      CAR_YEAR -> findAutoPartBuilder.year(update.text)
      else -> {}
    }
  }
}