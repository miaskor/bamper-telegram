package by.miaskor.bot.service.step.autopart.create

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.CreatingAutoPartStep
import by.miaskor.bot.domain.CreatingAutoPartStep.AUTO_PART
import by.miaskor.bot.domain.CreatingAutoPartStep.CAR
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.step.StepKeyboardBuilder
import com.pengrad.telegrambot.model.request.Keyboard

class CreatingAutoPartStepKeyboardBuilder(
  private val keyboardBuilder: KeyboardBuilder
) : StepKeyboardBuilder<CreatingAutoPartStep> {
  override fun buildStepKeyboard(step: CreatingAutoPartStep, keyboardSettings: KeyboardSettings): Keyboard {
    return when (step) {
      CAR, AUTO_PART -> keyboardBuilder.buildReplyKeyboard(keyboardSettings.keyboardForMandatorySteps())
      else -> keyboardBuilder.buildReplyKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
    }
  }
}