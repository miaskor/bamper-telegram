package by.miaskor.bot.service.carstep

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.CarBuilder
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.carstep.CarBuilderFieldResolver
import by.miaskor.bot.service.carstep.CreatingCarStepMessageResolver
import by.miaskor.bot.service.carstep.CreationCarStepValidation
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.text
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class ProcessingStepService(
  private val creationCarStepValidation: CreationCarStepValidation,
  private val keyboardBuilder: KeyboardBuilder,
  private val telegramBot: TelegramBot
) {

  fun process(
    update: Update,
    creatingCarStep: CreatingCarStep,
    carBuilder: CarBuilder,
    populateCache: (Update, CarBuilder) -> CarBuilder
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .filterWhen { creationCarStepValidation.validate(creatingCarStep, update.text, carBuilder) }
      .resolveLanguage(KeyboardSettings::class)
      .zipWith(CreatingCarStepMessageResolver.resolve(update, creatingCarStep.next(),carBuilder, true))
      .map { (keyboardSettings, message) ->
        val functionToEnrich = CarBuilderFieldResolver.resolve(creatingCarStep, carBuilder)
        populateCache.invoke(update, functionToEnrich(update.text).nextStep())
        val keyboard = keyboardBuilder.buildCreatingCarStepKeyboard(creatingCarStep.next(), keyboardSettings)
        telegramBot.sendMessageWithKeyboard(update.chatId, message, keyboard)
      }.switchIfEmpty(sendInvalidMessage(update,carBuilder, creatingCarStep))
  }

  private fun sendInvalidMessage(update: Update,carBuilder: CarBuilder, creatingCarStep: CreatingCarStep): Mono<Unit> {
    return Mono.just(update.chatId)
      .zipWith(CreatingCarStepMessageResolver.resolve(update, creatingCarStep,carBuilder, false))
      .map { (chatId, invalidMessage) ->
        telegramBot.sendMessage(chatId, invalidMessage)
      }.then(Mono.empty())
  }
}

