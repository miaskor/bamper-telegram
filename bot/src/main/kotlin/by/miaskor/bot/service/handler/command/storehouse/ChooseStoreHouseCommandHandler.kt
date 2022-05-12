package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.CHOOSING_STORE_HOUSE
import by.miaskor.bot.domain.Command.CHOOSE_STORE_HOUSE
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.addRowsToEnd
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.StoreHouseConnector
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import reactor.core.publisher.Mono

class ChooseStoreHouseCommandHandler(
  private val keyboardBuilder: KeyboardBuilder,
  private val storeHouseConnector: StoreHouseConnector,
  private val telegramBot: TelegramBot
) : CommandHandler {
  override val command = CHOOSE_STORE_HOUSE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, CHOOSING_STORE_HOUSE)
      .flatMap { storeHouseConnector.getAllByChatId(it) }
      .map { storeHouses -> storeHouses.map { it.name } }
      .flatMap { storeHouseNames ->
        val arrays = storeHouseNames.chunked(2).map {
          it.toTypedArray()
        }.toTypedArray()
        keyboardBuilder.build(update.chatId)
        .cast(ReplyKeyboardMarkup::class.java)
        .map { it.addRowsToEnd(arrays) }
      }
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map { telegramBot.sendMessageWithKeyboard(chatId, it.allStoreHousesMessage(), keyboard) }
  }
}
