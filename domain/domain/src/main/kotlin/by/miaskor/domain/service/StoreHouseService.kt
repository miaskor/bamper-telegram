package by.miaskor.domain.service

import by.miaskor.domain.api.domain.StoreHouseDto
import by.miaskor.domain.model.WorkerStoreHouseVO
import by.miaskor.domain.repository.StoreHouseRepository
import by.miaskor.domain.service.mapper.StoreHouseMapper
import by.miaskor.domain.tables.pojos.StoreHouse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class StoreHouseService(
  private val storeHouseRepository: StoreHouseRepository,
  private val workerStoreHouseService: WorkerStoreHouseService,
  private val storeHouseMapper: StoreHouseMapper,
) {

  fun getByNameAndTelegramChatId(storeHouseDto: StoreHouseDto): Mono<StoreHouseDto> {
    return storeHouseRepository.findByNameAndTelegramChatId(storeHouseDto.name, storeHouseDto.chatId)
      .map(storeHouseMapper::map)
  }

  fun getAllByChatId(chatId: Long): Mono<List<StoreHouseDto>> {
    return storeHouseRepository.findAllByChatId(chatId)
      .flatMapIterable { it }
      .map(storeHouseMapper::map)
      .mergeWith(
        workerStoreHouseService.getStoreHousesByChatId(chatId)
          .flatMapMany { getWorkerStoreHouses(it) }
      ).collectList()
  }

  private fun getWorkerStoreHouses(workerStoreHouses: List<WorkerStoreHouseVO>): Flux<StoreHouseDto> {
    return Mono.fromSupplier { workerStoreHouses.map { it.storeHouseId } }
      .flatMap { workerStoreHouseIds -> storeHouseRepository.findAllByIds(workerStoreHouseIds) }
      .flatMapMany { storeHouses ->
        Flux.fromIterable(
          storeHouses.map { storeHouse ->
            val workerStoreHouseVO = workerStoreHouses
              .find { workerStoreHouse -> workerStoreHouse.storeHouseId == storeHouse.id }
            StoreHouseDto(
              id = storeHouse.id ?: -1,
              chatId = storeHouse.telegramChatId ?: -1,
              name = storeHouse.storeHouseName ?: "",
              modifiable = workerStoreHouseVO?.privilege == "M"
            )
          }
        )
      }
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
