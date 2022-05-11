package by.miaskor.bot.service.handler.command.storehouse

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.STORE_HOUSE
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.StoreHouseConnector
import by.miaskor.domain.api.domain.StoreHouseDto
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class StoreHouseCommandHandler(
  private val telegramBot: TelegramBot,
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
          .then(sendSuccessMessage(update))
      ).flatMap { sendFailMessage(update) }
  }

  private fun sendFailMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(update.chatId, it.addingStoreHouseFailMessage())
      }.then(Mono.empty())
  }

  private fun sendSuccessMessage(update: Update): Mono<StoreHouseDto> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(update.chatId, it.addingStoreHouseSuccessMessage())
      }.then(Mono.empty())
  }
}
