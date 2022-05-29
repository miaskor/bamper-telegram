package by.miaskor.bot.service.handler.command.autopart

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.DELETE_AUTO_PART
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.AutoPartConnector
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class DeleteAutoPartCommandHandler(
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = DELETE_AUTO_PART
  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap { telegramClientCache.getTelegramClient(it) }
      .flatMap {
        autoPartConnector.deleteByStoreHouseIdAndId(it.currentStoreHouseId(), update.text.toLong())
          .switchIfEmpty(sendMessage(update.chatId, MessageSettings::deletingAutoPartNotFoundMessage))
      }.flatMap { success ->
        if (success)
          sendMessage(update.chatId, MessageSettings::deletingAutoPartSuccessMessage)
        else
          sendMessage(update.chatId, MessageSettings::deletingAutoPartFailMessage)
      }
  }
}
