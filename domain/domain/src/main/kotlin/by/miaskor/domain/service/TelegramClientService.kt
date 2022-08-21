package by.miaskor.domain.service

import by.miaskor.domain.api.domain.EmployeeExistsRequest
import by.miaskor.domain.api.domain.TelegramClientRequest
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.repository.TelegramClientRepository
import by.miaskor.domain.service.mapper.TelegramClientMapper
import by.miaskor.domain.tables.pojos.TelegramClient
import reactor.core.publisher.Mono

class TelegramClientService(
  private val telegramClientRepository: TelegramClientRepository,
  private val telegramClientMapper: TelegramClientMapper,
) {

  fun upsert(telegramClientRequest: TelegramClientRequest): Mono<Unit> {
    return telegramClientRepository.save(
      TelegramClient(
        chatId = telegramClientRequest.chatId,
        chatLanguage = telegramClientRequest.chatLanguage,
        userName = telegramClientRequest.username,
      )
    )
  }

  fun getAllEmployees(employerChatId: Long): Mono<List<TelegramClientResponse>> {
    return telegramClientRepository.findEmployeesByEmployerId(employerChatId)
      .flatMapIterable { it }
      .map(telegramClientMapper::map)
      .collectList()
  }

  fun getByChatId(chatId: Long): Mono<TelegramClientResponse> {
    return telegramClientRepository.findByChatId(chatId)
      .map(telegramClientMapper::map)
  }

  fun getByUsername(username: String): Mono<TelegramClientResponse> {
    return telegramClientRepository.findByUsername(username)
      .map(telegramClientMapper::map)
  }
}
