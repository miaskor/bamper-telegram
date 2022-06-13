package by.miaskor.bot.service.handler.state

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.AbstractStep
import by.miaskor.bot.domain.AbstractStepBuilder
import by.miaskor.bot.domain.Command
import by.miaskor.bot.domain.Command.NEXT_STEP
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.cache.Cache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.step.ProcessingStepService
import by.miaskor.bot.service.step.StepKeyboardBuilder
import by.miaskor.bot.service.step.StepMessageResolver
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

abstract class StepHandler<T>(
  private val cache: Cache<Long, AbstractStepBuilder<T>>,
  private val processingStepService: ProcessingStepService<T>,
  private val stepKeyboardBuilder: StepKeyboardBuilder<T>,
  private val stepMessageResolver: StepMessageResolver<T>,
  private val telegramBot: TelegramBot,
) where T : Enum<T>, T : AbstractStep<T> {

  fun handle(update: Update, defValue: AbstractStepBuilder<T>): Mono<Unit> {
    return Mono.fromSupplier { cache.get(update.chatId, defValue) }
      .flatMap { stepBuilder ->
        val currentStep = stepBuilder.currentStep()
        if (NEXT_STEP isCommand update.text && currentStep.isStepNotMandatory()) {
          val nextStep = stepBuilder.nextStep()
          cache.populate(update.chatId, nextStep)
          sendMessage(nextStep, update)
            .filter { currentStep.next().isFinalStep() }
            .flatMap { completeStep(update, stepBuilder) }
        } else if (NEXT_STEP isCommand update.text && !currentStep.isStepNotMandatory()) {
          sendMessage(stepBuilder, update)
        } else if (Command.PREVIOUS_STEP isCommand update.text) {
          val previousStep = stepBuilder.previousStep()
          cache.populate(update.chatId, previousStep)
          sendMessage(previousStep, update)
        } else {
          processingStepService.process(update, currentStep, stepBuilder)
            .filter { it && stepBuilder.currentStep().isFinalStep() }
            .flatMap { completeStep(update, stepBuilder) }
        }
      }
  }

  protected fun sendMessage(
    stepBuilder: AbstractStepBuilder<T>,
    update: Update
  ): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(KeyboardSettings::class)
      .zipWith(stepMessageResolver.resolve(update, stepBuilder.currentStep(), stepBuilder, true))
      .map { (keyboardSettings, message) ->
        val keyboard = stepKeyboardBuilder.buildStepKeyboard(stepBuilder.currentStep(), keyboardSettings)
        telegramBot.sendMessageWithKeyboard(update.chatId, message, keyboard)
      }
  }

  protected abstract fun completeStep(
    update: Update,
    stepBuilder: AbstractStepBuilder<T>
  ): Mono<Unit>
}