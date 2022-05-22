package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.StoreHouseDto
import by.miaskor.domain.service.StoreHouseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/store-house")
class StoreHouseController(
  private val storeHouseService: StoreHouseService
) {

  @GetMapping("/{chatId}/{storeHouseName}")
  fun getByNameAndTelegramChatId(@PathVariable chatId: Long, @PathVariable storeHouseName: String):
      Mono<ResponseEntity<StoreHouseDto>> {
    return Mono.fromSupplier {
      StoreHouseDto(
        chatId = chatId,
        name = storeHouseName
      )
    }
      .flatMap(storeHouseService::getByNameAndTelegramChatId)
      .map { ResponseEntity.ok(it) }
  }

  @PostMapping
  fun create(@RequestBody storeHouseDto: StoreHouseDto): Mono<ResponseEntity<Unit>> {
    return Mono.just(storeHouseDto)
      .flatMap { storeHouseService.create(it) }
      .then(Mono.just(ResponseEntity.ok().build()))
  }

  @GetMapping("/{chatId}")
  fun getAllByChatId(@PathVariable chatId: Long): Mono<ResponseEntity<List<StoreHouseDto>>> {
    return Mono.just(chatId)
      .flatMap(storeHouseService::getAllByChatId)
      .map { ResponseEntity.ok(it) }
  }
}
