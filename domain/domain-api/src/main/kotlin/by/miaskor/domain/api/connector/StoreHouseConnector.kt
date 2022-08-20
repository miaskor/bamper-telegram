package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.StoreHouseDto
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val BASE_URI = "/store-houses"

class StoreHouseConnector(
  private val webClient: WebClient,
) {

  fun create(storeHouseDto: StoreHouseDto): Mono<Unit> {
    return webClient.post()
      .uri(BASE_URI)
      .bodyValue(storeHouseDto)
      .retrieve()
      .bodyToMono()
  }

  fun getByNameAndTelegramChatId(storeHouseDto: StoreHouseDto): Mono<StoreHouseDto> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("storeHouseName", storeHouseDto.name)
          .queryParam("chatId", storeHouseDto.chatId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun getAllByChatId(chatId: Long): Mono<List<StoreHouseDto>> {
    return webClient.get()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("chatId", chatId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }
}
