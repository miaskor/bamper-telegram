package by.miaskor.domain.service

import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.repository.TelegramClientRepository
import by.miaskor.domain.tables.pojos.TelegramClient
import reactor.core.publisher.Mono

class TelegramClientService(
  private val telegramClientRepository: TelegramClientRepository
) {

  fun create(telegramClientRequest: TelegramClientRequest): Mono<Unit> {
    return telegramClientRepository.save(TelegramClient(
      chatId = telegramClientRequest.chatId,
      chatLanguage = telegramClientRequest.chatLanguage,
      nickName = telegramClientRequest.username,
    ))
  }
}
