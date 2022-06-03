package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.MODIFICATION_STORE_HOUSE_MENU
import by.miaskor.bot.domain.BotState.READ_ONLY_STORE_HOUSE_MENU
import by.miaskor.bot.domain.Command.SELECT_CERTAIN_STORE_HOUSE
import by.miaskor.bot.domain.StoreHouse
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.MessageSender.sendMessageWithKeyboard
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class SelectCertainStoreHouseCommandHandler(
  private val keyboardBuilder: KeyboardBuilder,
  private val telegramClientCache: TelegramClientCache,
  private val telegramBot: TelegramBot
) : CommandHandler {
  override val command = SELECT_CERTAIN_STORE_HOUSE

  override fun handle(update: Update): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .flatMapIterable { it.storeHouses() }
      .filter { storeHouse -> storeHouse.name == update.text }
      .next()
      .switchIfEmpty(sendMessage(update.chatId, MessageSettings::storeHouseNotFoundMessage))
      .flatMap { storeHouse ->
        telegramClientCache.getTelegramClient(update.chatId)
          .map { it.refreshCurrentStoreHouse(storeHouse) }
          .thenReturn(storeHouse)
      }
      .flatMap { storeHouse -> processMessage(update, storeHouse) }
  }

  private fun processMessage(update: Update, storeHouse: StoreHouse): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(
        update::chatId,
        if (storeHouse.modifiable) MODIFICATION_STORE_HOUSE_MENU else READ_ONLY_STORE_HOUSE_MENU
      )
      .then(sendMessageWithKeyboard(update.chatId, MessageSettings::storeHouseMenuMessage, update.text))
  }
}
