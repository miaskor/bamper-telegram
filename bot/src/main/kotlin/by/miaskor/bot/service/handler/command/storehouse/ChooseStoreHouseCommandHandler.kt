package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.CHOOSING_STORE_HOUSE
import by.miaskor.bot.domain.Command.CHOOSE_STORE_HOUSE
import by.miaskor.bot.domain.StoreHouse
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.MessageSender.sendMessageWithKeyboard
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.StoreHouseConnector
import by.miaskor.domain.api.domain.StoreHouseDto
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ChooseStoreHouseCommandHandler(
  private val storeHouseConnector: StoreHouseConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = CHOOSE_STORE_HOUSE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, CHOOSING_STORE_HOUSE)
      .flatMap { storeHouseConnector.getAllByChatId(it) }
      .flatMap { handle(it, update) }
  }

  private fun handle(storeHouseDtos: List<StoreHouseDto>, update: Update): Mono<Unit> {
    return Flux.fromIterable(storeHouseDtos)
      .map {
        StoreHouse(
          name = it.name,
          id = it.id,
          modifiable = it.modifiable
        )
      }.collectList()
      .flatMap { storeHouses ->
        telegramClientCache.getTelegramClient(update.chatId)
          .map { it.refreshStoreHouses(storeHouses.toSet()) }
      }
      .then(sendMessageWithKeyboard(update.chatId, MessageSettings::allStoreHousesMessage))
  }
}
