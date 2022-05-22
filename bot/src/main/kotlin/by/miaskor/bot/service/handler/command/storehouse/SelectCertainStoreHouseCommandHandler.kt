package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command.SELECT_CERTAIN_STORE_HOUSE
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
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
      .map { it.telegramClientStoreHouses }
      .flatMapIterable { it.storeHouses.entries }
      .filter { storeHouseName -> storeHouseName.key == update.text }
      .next()
      .switchIfEmpty(storeHouseNotFound(update))
      .flatMap { storeHouseName ->
        telegramClientCache.getTelegramClient(update.chatId)
          .map { it.refreshCurrentStoreHouse(Pair(storeHouseName.key, storeHouseName.value)) }
          .thenReturn(storeHouseName)
      }
      .flatMap { processMessage(update) }
  }

  private fun storeHouseNotFound(update: Update): Mono<out Map.Entry<String, Long>> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(
          update.chatId, it.storeHouseNotFoundMessage().format(update.text)
        )
      }.then(Mono.empty())
  }

  private fun processMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, BotState.STORE_HOUSE_MENU)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map {
        telegramBot.sendMessageWithKeyboard(
          update.chatId, it.t1.storeHouseMenuMessage().format(update.text), it.t2
        )
      }.then(Mono.empty())
  }
}
