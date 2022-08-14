package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import by.miaskor.domain.api.domain.StoreHouseRequestWithConstraint
import by.miaskor.domain.service.AutoPartService
import by.miaskor.domain.service.StoreHouseConstraintHandler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auto-part")
class AutoPartController(
  private val autoPartService: AutoPartService,
  private val storeHouseConstraintHandler: StoreHouseConstraintHandler,
) {

  @PostMapping
  fun create(@RequestBody autoPartDto: AutoPartDto): Mono<ResponseEntity<Unit>> {
    return autoPartService.create(autoPartDto)
      .then(Mono.just(ResponseEntity.ok().build()))
  }

  @PostMapping("/list")
  fun getAllByStoreHouseId(
    @RequestBody storeHouseIdRequest: StoreHouseIdRequest,
  ): Mono<ResponseEntity<ResponseWithLimit<AutoPartWithPhotoResponse>>> {
    return autoPartService.getAllByStoreHouseId(storeHouseIdRequest)
      .map { ResponseEntity.ok(it) }
  }

  @PostMapping("/list/constraint")
  fun getAllByStoreHouseIdWithConstraint(
    @RequestBody storeHouseIdRequest: StoreHouseRequestWithConstraint,
  ): Mono<ResponseEntity<ResponseWithLimit<AutoPartWithPhotoResponse>>> {
    return storeHouseConstraintHandler.handle(storeHouseIdRequest)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/store-house")
  fun getByStoreHouseIdAndId(
    @RequestParam storeHouseId: Long,
    @RequestParam autoPartId: Long,
  ): Mono<ResponseEntity<AutoPartWithPhotoResponse>> {
    return autoPartService.getByStoreHouseIdAndId(storeHouseId, autoPartId)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/telegram-chat")
  fun getByTelegramChatIdAndId(
    @RequestParam telegramChatId: Long,
    @RequestParam autoPartId: Long,
  ): Mono<ResponseEntity<AutoPartResponse>> {
    return autoPartService.getByTelegramChatIdAndId(telegramChatId, autoPartId)
      .map { ResponseEntity.ok(it) }
  }

  @DeleteMapping
  fun deleteById(@RequestParam storeHouseId: Long, @RequestParam autoPartId: Long): Mono<ResponseEntity<Boolean>> {
    return autoPartService.deleteByStoreHouseIdAndId(storeHouseId, autoPartId)
      .map { ResponseEntity.ok(it) }
  }
}
