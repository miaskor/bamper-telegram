package by.miaskor.bot.service.handler.command.car

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.handler.command.CommandHandler
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import reactor.core.publisher.Mono

class CreateCarCommandHandler (
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : CommandHandler {
  override val command = Command.CREATE_CAR

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, BotState.CREATING_CAR)
      .flatMap(keyboardBuilder::build)
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map { telegramBot.sendMessageWithKeyboard(chatId, it.creatingCarMessage(), keyboard) }
  }
}
