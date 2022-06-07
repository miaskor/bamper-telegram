package by.miaskor.bot.service.step.autopart.create

import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.AutoPartBuilder
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingAutoPartStep.CAR
import by.miaskor.bot.domain.CreatingAutoPartStep.CURRENCY
import by.miaskor.bot.domain.CreatingAutoPartStep.DESCRIPTION
import by.miaskor.bot.domain.CreatingAutoPartStep.PART_NUMBER
import by.miaskor.bot.domain.CreatingAutoPartStep.PHOTO
import by.miaskor.bot.domain.CreatingAutoPartStep.PRICE
import by.miaskor.bot.domain.CreatingAutoPartStep.QUALITY
import by.miaskor.bot.service.extension.photoId
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.step.StepBuilderFieldEnricher
import com.pengrad.telegrambot.model.Update

class AutoPartBuilderFieldEnricher : StepBuilderFieldEnricher<CreatingAutoPartStep> {
  override fun enrich(
    update: Update,
    step: CreatingAutoPartStep,
    stepBuilder: AbstractStepBuilder<CreatingAutoPartStep>
  ) {
    val autoPartBuilder = stepBuilder as AutoPartBuilder
    when (step) {
      CAR -> autoPartBuilder.carId(update.text.toLong())
      PART_NUMBER -> autoPartBuilder.partNumber(update.text)
      DESCRIPTION -> autoPartBuilder.description(update.text)
      PRICE -> autoPartBuilder.price(update.text)
      CURRENCY -> autoPartBuilder.currency(update.text)
      QUALITY -> autoPartBuilder.quality(update.text)
      PHOTO -> autoPartBuilder.photoId(update.photoId)
      else -> {}
    }
  }
}