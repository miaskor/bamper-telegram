package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.CreatingCarMessageSettings
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.CreatingCarStep.BODY
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_CAPACITY
import by.miaskor.bot.domain.CreatingCarStep.ENGINE_TYPE
import by.miaskor.bot.domain.CreatingCarStep.FUEL_TYPE
import by.miaskor.bot.domain.CreatingCarStep.MODEL
import by.miaskor.bot.domain.CreatingCarStep.TRANSMISSION
import by.miaskor.bot.domain.CreatingCarStep.YEAR
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

object CreatingCarStepMessageResolver {

  fun resolve(update: Update, creatingCarStep: CreatingCarStep, isValid: Boolean): Mono<String> {
    return Mono.just(update.chatId)
      .resolveLanguage(CreatingCarMessageSettings::class)
      .flatMap { creatingCarMessage ->
        if (isValid)
          resolveMessage(creatingCarStep, creatingCarMessage)
        else
          resolveInvalidMessage(creatingCarStep, creatingCarMessage)
      }
  }

  private fun resolveMessage(
    creatingCarStep: CreatingCarStep,
    creatingCarMessage: CreatingCarMessageSettings
  ): Mono<String> {
    return Mono.fromSupplier {
      when (creatingCarStep) {
        BRAND_NAME -> creatingCarMessage.brandNameMessage()
        MODEL -> creatingCarMessage.modelMessage()
        YEAR -> creatingCarMessage.yearMessage()
        BODY -> creatingCarMessage.bodyMessage()
        TRANSMISSION -> creatingCarMessage.transmissionMessage()
        ENGINE_CAPACITY -> creatingCarMessage.engineCapacityMessage()
        FUEL_TYPE -> creatingCarMessage.fuelTypeMessage()
        ENGINE_TYPE -> creatingCarMessage.engineTypeMessage()
      }
    }
  }

  private fun resolveInvalidMessage(
    creatingCarStep: CreatingCarStep,
    creatingCarMessage: CreatingCarMessageSettings
  ): Mono<String> {
    return Mono.fromSupplier {
      when (creatingCarStep) {
        BRAND_NAME -> creatingCarMessage.invalidBrandNameMessage()
        MODEL -> creatingCarMessage.invalidModelMessage()
        YEAR -> creatingCarMessage.invalidYearMessage()
        BODY -> creatingCarMessage.invalidBodyMessage()
        TRANSMISSION -> creatingCarMessage.invalidTransmissionMessage()
        ENGINE_CAPACITY -> creatingCarMessage.invalidEngineCapacityMessage()
        FUEL_TYPE -> creatingCarMessage.invalidFuelTypeMessage()
        ENGINE_TYPE -> creatingCarMessage.invalidEngineTypeMessage()
      }
    }
  }
}
