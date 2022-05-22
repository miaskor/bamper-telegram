package by.miaskor.domain.service

import by.miaskor.domain.api.domain.StoreHouseDto
import by.miaskor.domain.repository.StoreHouseRepository
import by.miaskor.domain.tables.pojos.StoreHouse
import reactor.core.publisher.Mono

class StoreHouseService(
  private val storeHouseRepository: StoreHouseRepository
) {

  fun getByNameAndTelegramChatId(storeHouseDto: StoreHouseDto): Mono<StoreHouseDto> {
    return storeHouseRepository.findByNameAndTelegramChatId(storeHouseDto.name, storeHouseDto.chatId)
      .map {
        StoreHouseDto(
          id = it.id ?: -1,
          name = it.storeHouseName ?: "",
          chatId = it.telegramChatId ?: -1
        )
      }
  }

  fun getAllByChatId(chatId: Long): Mono<List<StoreHouseDto>> {
    return storeHouseRepository.findAllByChatId(chatId)
      .flatMapIterable { it }
      .map {
        StoreHouseDto(
          id = it.id ?: -1,
          chatId = it.telegramChatId ?: -1,
          name = it.storeHouseName ?: ""
        )
      }.collectList()
  }

  fun create(storeHouseDto: StoreHouseDto): Mono<Unit> {
    return Mono.fromSupplier {
      StoreHouse(
        storeHouseName = storeHouseDto.name,
        telegramChatId = storeHouseDto.chatId
      )
    }.flatMap(storeHouseRepository::save)
  }
}
