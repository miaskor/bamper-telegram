package by.miaskor.domain.mapper

import by.miaskor.domain.tables.pojos.TelegramClient
import by.miaskor.domain.tables.records.TelegramClientRecord
import reactor.core.publisher.Mono

object TelegramClientMapper : EntityMapper<TelegramClient, TelegramClientRecord> {
  override fun map(from: TelegramClient): Mono<TelegramClientRecord> {
    return Mono.fromSupplier {
      TelegramClientRecord().apply {
        id = null
        chatId = from.chatId
        chatLanguage = from.chatLanguage
        nickName = from.nickName
        bamperClientId = from.bamperClientId
      }
    }
  }
}
