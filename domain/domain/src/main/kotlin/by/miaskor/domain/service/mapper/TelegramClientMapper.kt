package by.miaskor.domain.service.mapper

import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.tables.pojos.TelegramClient

class TelegramClientMapper {

  fun map(telegramClient: TelegramClient): TelegramClientResponse {
    return TelegramClientResponse(
      chatId = telegramClient.chatId ?: -1,
      chatLanguage = telegramClient.chatLanguage ?: "",
      username = telegramClient.userName ?: "",
      bamperClientId = telegramClient.bamperClientId
    )
  }
}
