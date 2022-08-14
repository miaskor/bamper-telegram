package by.miaskor.domain.api.connector

import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import by.miaskor.domain.api.domain.StoreHouseRequestWithConstraint
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private const val BASE_URI = "/auto-part"

class AutoPartConnector(
  private val webClient: WebClient,
) {

  fun create(autoPartDto: AutoPartDto): Mono<Unit> {
    return webClient.post()
      .uri(BASE_URI)
      .body(BodyInserters.fromValue(autoPartDto))
      .retrieve()
      .bodyToMono()
  }

  fun getByStoreHouseIdAndId(storeHouseId: Long, autoPartId: Long): Mono<AutoPartWithPhotoResponse> {
    return webClient.get()
      .uri("$BASE_URI/store-house") { uriBuilder ->
        uriBuilder.queryParam("storeHouseId", storeHouseId)
          .queryParam("autoPartId", autoPartId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun getByTelegramChatIdAndId(telegramChatId: Long, autoPartId: Long): Mono<AutoPartResponse> {
    return webClient.get()
      .uri("$BASE_URI/telegram-chat") { uriBuilder ->
        uriBuilder.queryParam("telegramChatId", telegramChatId)
          .queryParam("autoPartId", autoPartId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun deleteByStoreHouseIdAndId(storeHouseId: Long, autoPartId: Long): Mono<Boolean> {
    return webClient.delete()
      .uri(BASE_URI) { uriBuilder ->
        uriBuilder.queryParam("storeHouseId", storeHouseId)
          .queryParam("autoPartId", autoPartId)
          .build()
      }
      .retrieve()
      .bodyToMono()
  }

  fun getAllByStoreHouseId(storeHouseIdRequest: StoreHouseIdRequest): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return webClient.post()
      .uri("$BASE_URI/list")
      .body(BodyInserters.fromValue(storeHouseIdRequest))
      .retrieve()
      .bodyToMono()
  }

  fun getAllByConstraint(
    storeHouseIdRequest: StoreHouseRequestWithConstraint,
  ): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return webClient.post()
      .uri("$BASE_URI/list/constraint")
      .body(BodyInserters.fromValue(storeHouseIdRequest))
      .retrieve()
      .bodyToMono()
  }
}
