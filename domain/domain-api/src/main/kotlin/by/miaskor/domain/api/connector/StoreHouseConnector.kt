package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.StoreHouseDto
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class StoreHouseConnector(
  private val webClient: WebClient
) {

  fun create(storeHouseDto: StoreHouseDto): Mono<Unit> {
    return webClient.post()
      .uri("/store-house")
      .bodyValue(storeHouseDto)
      .retrieve()
      .bodyToMono()
  }

  fun getByNameAndTelegramChatId(storeHouseDto: StoreHouseDto): Mono<StoreHouseDto> {
    return webClient.get()
      .uri("/store-house/${storeHouseDto.chatId}/${storeHouseDto.name}")
      .retrieve()
      .bodyToMono()
  }

  fun getAllByChatId(chatId: Long): Mono<List<StoreHouseDto>> {
    return webClient.get()
      .uri("/store-house/$chatId")
      .retrieve()
      .bodyToMono()
  }
}
