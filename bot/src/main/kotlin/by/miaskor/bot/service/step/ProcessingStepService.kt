package by.miaskor.bot.service.step

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.AbstractStep
import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class ProcessingStepService<T>(
  private val stepValidator: StepValidator<T>,
  private val stepMessageResolver: StepMessageResolver<T>,
  private val stepBuilderFieldEnricher: StepBuilderFieldEnricher<T>,
  private val stepKeyboardBuilder: StepKeyboardBuilder<T>,
  private val stepCache: Cache<Long, AbstractStepBuilder<T>>,
  private val telegramBot: TelegramBot
) where T : Enum<T>, T : AbstractStep<T> {

  fun process(
    update: Update,
    step: T,
    stepBuilder: AbstractStepBuilder<T>
  ): Mono<Boolean> {
    return Mono.just(update.chatId)
      .filterWhen { stepValidator.validate(step, update, stepBuilder) }
      .resolveLanguage(KeyboardSettings::class)
      .zipWith(stepMessageResolver.resolve(update, step.next(), stepBuilder, true))
      .doOnNext { stepBuilderFieldEnricher.enrich(update, step, stepBuilder) }
      .doOnNext { stepCache.populate(update.chatId, stepBuilder.nextStep()) }
      .map { (keyboardSettings, message) ->
        val keyboard = stepKeyboardBuilder.buildStepKeyboard(step.next(), keyboardSettings)
        telegramBot.sendMessageWithKeyboard(update.chatId, message, keyboard)
        true
      }.switchIfEmpty(sendInvalidMessage(update, stepBuilder, step))
  }

  private fun sendInvalidMessage(update: Update, stepBuilder: AbstractStepBuilder<T>, step: T): Mono<Boolean> {
    return Mono.just(update.chatId)
      .zipWith(stepMessageResolver.resolve(update, step, stepBuilder, false))
      .map { (chatId, invalidMessage) -> telegramBot.sendMessage(chatId, invalidMessage) }
      .then(Mono.just(false))
  }
}

