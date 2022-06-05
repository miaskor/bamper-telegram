package by.miaskor.bot.service.step.car

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.CreatingCarStep
import by.miaskor.bot.domain.CreatingCarStep.BRAND_NAME
import by.miaskor.bot.domain.CreatingCarStep.MODEL
import by.miaskor.bot.domain.CreatingCarStep.YEAR
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.step.StepKeyboardBuilder
import com.pengrad.telegrambot.model.request.Keyboard

class CreatingCarStepKeyboardBuilder(
  private val keyboardBuilder: KeyboardBuilder
) : StepKeyboardBuilder<CreatingCarStep> {
  override fun buildStepKeyboard(step: CreatingCarStep, keyboardSettings: KeyboardSettings): Keyboard {
    return when (step) {
      BRAND_NAME, MODEL, YEAR -> keyboardBuilder.buildReplyKeyboard(keyboardSettings.keyboardForMandatorySteps())
      else -> keyboardBuilder.buildReplyKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
    }
  }
}