package by.miaskor.bot.service.handler.command.car

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.DELETE_CAR
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.CarConnector
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class DeleteCarCommandHandler(
  private val carConnector: CarConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = DELETE_CAR
  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap { telegramClientCache.getTelegramClient(it) }
      .flatMap {
        carConnector.deleteByStoreHouseIdAndId(it.currentStoreHouseId(), update.text.toLong())
          .switchIfEmpty(sendMessage(update.chatId, MessageSettings::deletingCarNotFoundMessage, update.text))
      }.flatMap { success ->
        if (success)
          sendMessage(update.chatId, MessageSettings::deletingCarSuccessMessage)
        else
          sendMessage(update.chatId, MessageSettings::deletingCarFailMessage)
      }
  }
}
