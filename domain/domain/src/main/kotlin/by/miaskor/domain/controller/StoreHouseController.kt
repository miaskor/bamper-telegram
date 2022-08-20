package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.StoreHouseDto
import by.miaskor.domain.service.StoreHouseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/store-houses")
class StoreHouseController(
  private val storeHouseService: StoreHouseService,
) {

  @GetMapping(params = ["chatId", "storeHouseName"])
  fun getByNameAndTelegramChatId(@RequestParam chatId: Long, @RequestParam storeHouseName: String):
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

  @GetMapping(params = ["chatId"])
  fun getAllByChatId(@RequestParam chatId: Long): Mono<ResponseEntity<List<StoreHouseDto>>> {
    return Mono.just(chatId)
      .flatMap(storeHouseService::getAllByChatId)
      .map { ResponseEntity.ok(it) }
  }
}
