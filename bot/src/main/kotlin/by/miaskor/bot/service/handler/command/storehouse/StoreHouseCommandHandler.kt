package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.STORE_HOUSE
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.StoreHouseConnector
import by.miaskor.domain.api.domain.StoreHouseDto
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class StoreHouseCommandHandler(
  private val storeHouseConnector: StoreHouseConnector
) : CommandHandler {
  override val command = STORE_HOUSE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier {
      StoreHouseDto(
        name = update.text,
        chatId = update.chatId
      )
    }.flatMap { createStoreHouse(it, update) }
  }

  private fun createStoreHouse(storeHouseDto: StoreHouseDto, update: Update): Mono<Unit> {
    return Mono.just(storeHouseDto)
      .flatMap(storeHouseConnector::getByNameAndTelegramChatId)
      .switchIfEmpty(
        Mono.just(storeHouseDto)
          .flatMap(storeHouseConnector::create)
          .then(sendMessage(update.chatId, MessageSettings::addingStoreHouseSuccessMessage))
      ).flatMap { sendMessage(update.chatId, MessageSettings::addingStoreHouseFailMessage) }
  }
}
