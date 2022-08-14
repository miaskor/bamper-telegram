package by.miaskor.bot.service.handler.command.bamper

import by.miaskor.bot.domain.Command.IMPORT_AUTO_PART_BY_ID
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.connector.BamperIntegrationConnector
import by.miaskor.domain.ImportAdvertisementRequest
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class ImportAutoPartByIdCommandHandler(
  private val telegramClientCache: TelegramClientCache,
  private val bamperIntegrationConnector: BamperIntegrationConnector,
) : CommandHandler {
  override val command = IMPORT_AUTO_PART_BY_ID

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.text)
      .zipWith(telegramClientCache.getTelegramClient(update.chatId))
      .map { (autoPartId, telegramClient) ->
        ImportAdvertisementRequest(
          autoPartId = autoPartId.toLong(),
          telegramCharId = telegramClient.chatId,
          bamperSessionId = telegramClient.bamperSessionId,
          article = "lol"
        )
      }.flatMap(bamperIntegrationConnector::importAdvertisement)
  }
}
