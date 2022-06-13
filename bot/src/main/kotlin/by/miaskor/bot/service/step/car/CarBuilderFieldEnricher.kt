package by.miaskor.bot.service.step.car

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.CreatingCarStep.BODY
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_CAPACITY
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_TYPE
import by.miaskor.bot.domain.CreatingCarStep.FUEL_TYPE
import by.miaskor.bot.domain.CreatingCarStep.TRANSMISSION
import by.miaskor.bot.domain.CreatingCarStep.YEAR
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.step.StepBuilderFieldEnricher
import com.pengrad.telegrambot.model.Update

class CarBuilderFieldEnricher : StepBuilderFieldEnricher<CreatingCarStep> {

  override fun enrich(
    update: Update,
    step: CreatingCarStep,
    stepBuilder: AbstractStepBuilder<CreatingCarStep>
  ) {
    val carBuilder = stepBuilder as CarBuilder
    when (step) {
      BRAND_NAME -> carBuilder.brandName(update.text)
      YEAR -> carBuilder.year(update.text)
      BODY -> carBuilder.body(update.text)
      TRANSMISSION -> carBuilder.transmission(update.text)
      ENGINE_CAPACITY -> carBuilder.engineCapacity(update.text)
      FUEL_TYPE -> carBuilder.fuelType(update.text)
      ENGINE_TYPE -> carBuilder.engineType(update.text)
      else -> {}
    }
  }
}
