package by.miaskor.bot.service.step.autopart.create

import by.miaskor.bot.configuration.settings.CreatingAutoPartMessageSettings
import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.step.StepMessageResolver
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class CreatingAutoPartStepMessageResolver : StepMessageResolver<CreatingAutoPartStep> {
  override fun resolve(
    update: Update,
    step: CreatingAutoPartStep,
    stepBuilder: AbstractStepBuilder<CreatingAutoPartStep>,
    isValid: Boolean
  ): Mono<String> {
    return Mono.just(update.chatId)
      .resolveLanguage(CreatingAutoPartMessageSettings::class)
      .flatMap { creatingAutoPartMessage ->
        if (isValid)
          resolveMessage(step, creatingAutoPartMessage)
        else
          resolveInvalidMessage(step, creatingAutoPartMessage)
      }
  }

  private fun resolveMessage(
    step: CreatingAutoPartStep,
    creatingAutoPartMessage: CreatingAutoPartMessageSettings
  ): Mono<String> {
    return Mono.fromSupplier {
      when (step) {
        CreatingAutoPartStep.CAR -> creatingAutoPartMessage.carMessage()
        CreatingAutoPartStep.AUTO_PART -> creatingAutoPartMessage.autoPartMessage()
        CreatingAutoPartStep.PART_NUMBER -> creatingAutoPartMessage.partNumberMessage()
        CreatingAutoPartStep.DESCRIPTION -> creatingAutoPartMessage.descriptionMessage()
        CreatingAutoPartStep.PRICE -> creatingAutoPartMessage.priceMessage()
        CreatingAutoPartStep.CURRENCY -> creatingAutoPartMessage.currencyMessage()
        CreatingAutoPartStep.QUALITY -> creatingAutoPartMessage.qualityMessage()
        CreatingAutoPartStep.PHOTO -> creatingAutoPartMessage.photoMessage()
        CreatingAutoPartStep.COMPLETE -> ""
      }
    }
  }

  private fun resolveInvalidMessage(
    step: CreatingAutoPartStep,
    creatingAutoPartMessage: CreatingAutoPartMessageSettings
  ): Mono<String> {
    return Mono.fromSupplier {
      when (step) {
        CreatingAutoPartStep.CAR -> creatingAutoPartMessage.carInvalidMessage()
        CreatingAutoPartStep.AUTO_PART -> creatingAutoPartMessage.autoPartInvalidMessage()
        CreatingAutoPartStep.PRICE -> creatingAutoPartMessage.autoPartInvalidMessage()
        CreatingAutoPartStep.CURRENCY -> creatingAutoPartMessage.currencyInvalidMessage()
        CreatingAutoPartStep.QUALITY -> creatingAutoPartMessage.qualityInvalidMessage()
        CreatingAutoPartStep.PHOTO -> creatingAutoPartMessage.photoInvalidMessage()
        else -> ""
      }
    }
  }
}