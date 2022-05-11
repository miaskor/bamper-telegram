package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.StateSettings
import by.miaskor.bot.domain.BotState.CREATING_STORE_HOUSE
import by.miaskor.bot.domain.Command.CREATE_STORE_HOUSE
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

class CreateStoreHouseCommandHandler(
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder
) : CommandHandler {
  override val command = CREATE_STORE_HOUSE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, CREATING_STORE_HOUSE)
      .flatMap(keyboardBuilder::build)
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(StateSettings::class)
      .map { telegramBot.sendMessageWithKeyboard(chatId, it.storeHouseMessage(), keyboard) }
  }
}
