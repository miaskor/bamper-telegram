package by.miaskor.bot.service.step.autopart.find

import by.miaskor.bot.configuration.settings.FindingAutoPartMessageSettings
import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.FindingAutoPartStep
import by.miaskor.bot.domain.FindingAutoPartStep.AUTO_PART
import by.miaskor.bot.domain.FindingAutoPartStep.AUTO_PART_NUMBER
import by.miaskor.bot.domain.FindingAutoPartStep.BRAND
import by.miaskor.bot.domain.FindingAutoPartStep.CAR_YEAR
import by.miaskor.bot.domain.FindingAutoPartStep.COMPLETE
import by.miaskor.bot.domain.FindingAutoPartStep.MODEL
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.step.StepMessageResolver
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class FindingAutoPartStepMessageResolver : StepMessageResolver<FindingAutoPartStep> {
  override fun resolve(
    update: Update,
    step: FindingAutoPartStep,
    stepBuilder: AbstractStepBuilder<FindingAutoPartStep>,
    isValid: Boolean
  ): Mono<String> {
    return Mono.just(update.chatId)
      .resolveLanguage(FindingAutoPartMessageSettings::class)
      .flatMap { findingAutoPartMessageSettings ->
        if (isValid)
          resolveMessage(step, findingAutoPartMessageSettings)
        else
          resolveInvalidMessage(step, findingAutoPartMessageSettings)
      }
  }

  private fun resolveMessage(
    step: FindingAutoPartStep,
    findingAutoPartMessageSettings: FindingAutoPartMessageSettings
  ): Mono<String> {
    return Mono.fromSupplier {
      when (step) {
        AUTO_PART -> findingAutoPartMessageSettings.autoPartMessage()
        AUTO_PART_NUMBER -> findingAutoPartMessageSettings.partNumberMessage()
        BRAND -> findingAutoPartMessageSettings.brandMessage()
        MODEL -> findingAutoPartMessageSettings.modelMessage()
        CAR_YEAR -> findingAutoPartMessageSettings.yearMessage()
        COMPLETE -> ""
      }
    }
  }

  private fun resolveInvalidMessage(
    step: FindingAutoPartStep,
    findingAutoPartMessageSettings: FindingAutoPartMessageSettings
  ): Mono<String> {
    return Mono.just(findingAutoPartMessageSettings.notFoundMessage())
  }
}