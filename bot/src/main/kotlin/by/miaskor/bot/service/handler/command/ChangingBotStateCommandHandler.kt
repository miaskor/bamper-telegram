package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import reactor.core.publisher.Mono
import kotlin.reflect.KFunction1

class ChangingBotStateCommandHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder,
  override val command: Command,
  private val changeToState: BotState,
  private val messageFunction: KFunction1<MessageSettings, String>
) : CommandHandler {
  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, changeToState)
      .flatMap(keyboardBuilder::build)
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map { telegramBot.sendMessageWithKeyboard(chatId, messageFunction.invoke(it), keyboard) }
  }
}
