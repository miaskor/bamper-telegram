package by.miaskor.bot.service.step.autopart.find

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.FindingAutoPartStep
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.step.StepKeyboardBuilder
import com.pengrad.telegrambot.model.request.Keyboard

class FindingAutoPartStepKeyboardBuilder(
  private val keyboardBuilder: KeyboardBuilder
) : StepKeyboardBuilder<FindingAutoPartStep> {
  override fun buildStepKeyboard(step: FindingAutoPartStep, keyboardSettings: KeyboardSettings): Keyboard {
    return keyboardBuilder.buildReplyKeyboard(keyboardSettings.keyboardForNotMandatorySteps())
  }
}