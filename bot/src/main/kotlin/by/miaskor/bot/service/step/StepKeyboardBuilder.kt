package by.miaskor.bot.service.step

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.domain.AbstractStep
import com.pengrad.telegrambot.model.request.Keyboard

interface StepKeyboardBuilder<T> where T : Enum<T>, T : AbstractStep<T> {

  fun buildStepKeyboard(step: T, keyboardSettings: KeyboardSettings): Keyboard
}