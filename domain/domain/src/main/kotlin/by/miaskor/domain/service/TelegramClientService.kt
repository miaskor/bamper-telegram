package by.miaskor.domain.service

import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.repository.TelegramClientRepository
import by.miaskor.domain.tables.pojos.TelegramClient
import reactor.core.publisher.Mono

class TelegramClientService(
  private val telegramClientRepository: TelegramClientRepository
) {

  fun upsert(telegramClientRequest: TelegramClientRequest): Mono<Unit> {
    return telegramClientRepository.save(
      TelegramClient(
        chatId = telegramClientRequest.chatId,
        chatLanguage = telegramClientRequest.chatLanguage,
        nickName = telegramClientRequest.username,
      )
    )
  }

  fun getByChatId(chatId: Long): Mono<TelegramClientResponse> {
    return telegramClientRepository.findByChatId(chatId)
      .map {
        TelegramClientResponse(
          it.chatId?.toLong() ?: -1,
          it.chatLanguage ?: "",
          it.bamperClientId
        )
      }
  }
}
