package by.miaskor.bot.service.handler.command.autopart

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.FIND_AUTO_PART_BY_PART_ID_ENTITY
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.handler.list.ListEntitySender.sendEntity
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class FindAutoPartByIdCommandHandler(
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = FIND_AUTO_PART_BY_PART_ID_ENTITY

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { telegramClient -> handle(update, telegramClient) }
  }

  private fun handle(update: Update, telegramClient: TelegramClient): Mono<Unit> {
    return Mono.just(telegramClient)
      .flatMap { autoPartConnector.getByStoreHouseIdAndId(telegramClient.currentStoreHouseId(), update.text.toLong()) }
      .flatMap {
        sendEntity(update.chatId, it, MessageSettings::listAutoPartMessage)
        { AutoPartWithPhotoResponse.disassembly(it, telegramClient.chatLanguage) }
      }.then(Mono.empty())
  }
}
