package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.CHOOSING_STORE_HOUSE
import by.miaskor.bot.domain.Command.CHOOSE_STORE_HOUSE
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.StoreHouseConnector
import by.miaskor.domain.api.domain.StoreHouseDto
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.Keyboard
import reactor.core.publisher.Mono

class ChooseStoreHouseCommandHandler(
  private val keyboardBuilder: KeyboardBuilder,
  private val storeHouseConnector: StoreHouseConnector,
  private val telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = CHOOSE_STORE_HOUSE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, CHOOSING_STORE_HOUSE)
      .flatMap { storeHouseConnector.getAllByChatId(it) }
      .flatMap { handle(it, update) }
  }

  private fun handle(storeHouses: List<StoreHouseDto>, update: Update): Mono<Unit> {
    return Mono.fromSupplier { storeHouses.map { it.name } }
      .flatMap { storeHouseNames ->
        telegramClientCache.getTelegramClient(update.chatId)
          .map { it.refreshStoreHouses(storeHouseNames) }
          .thenReturn(storeHouseNames)
      }
      .flatMap { keyboardBuilder.build(update.chatId) }
      .flatMap { handle(it, update.chatId) }
  }

  private fun handle(keyboard: Keyboard, chatId: Long): Mono<Unit> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map { telegramBot.sendMessageWithKeyboard(chatId, it.allStoreHousesMessage(), keyboard) }
  }
}
