package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.ConstraintType
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdWithConstraint
import by.miaskor.domain.api.domain.StoreHouseIdWithLimitRequest
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
@RequestMapping("/auto-parts")
class AutoPartController(
  private val autoPartService: AutoPartService,
  private val storeHouseConstraintHandler: StoreHouseConstraintHandler,
) {

  @PostMapping
  fun create(@RequestBody autoPartDto: AutoPartDto): Mono<ResponseEntity<Unit>> {
    return autoPartService.create(autoPartDto)
      .thenReturn(ResponseEntity.ok().build())
  }

  @GetMapping(params = ["storeHouseId", "limit", "offset"])
  fun getAllByStoreHouseId(
    @RequestParam storeHouseId: Long,
    @RequestParam limit: Long,
    @RequestParam offset: Long,
  ): Mono<ResponseEntity<ResponseWithLimit<AutoPartWithPhotoResponse>>> {
    return Mono.fromSupplier {
      StoreHouseIdWithLimitRequest(
        storeHouseId = storeHouseId,
        limit = limit,
        offset = offset
      )
    }.flatMap(autoPartService::getAllByStoreHouseId)
      .map { response -> ResponseEntity.ok(response) }
  }

  @GetMapping(params = ["storeHouseId", "limit", "offset", "constraint", "constraintType"])
  fun getAllByStoreHouseIdWithConstraint(
    @RequestParam storeHouseId: Long,
    @RequestParam limit: Long,
    @RequestParam offset: Long,
    @RequestParam constraint: String,
    @RequestParam constraintType: ConstraintType,
  ): Mono<ResponseEntity<ResponseWithLimit<AutoPartWithPhotoResponse>>> {

    return Mono.fromSupplier {
      StoreHouseIdWithConstraint(
        storeHouseId = storeHouseId,
        limit = limit,
        offset = offset,
        constraintType = constraintType,
        constraint = constraint
      )
    }.flatMap(storeHouseConstraintHandler::handle)
      .map { response -> ResponseEntity.ok(response) }
  }

  @GetMapping(params = ["storeHouseId", "autoPartId"])
  fun getByStoreHouseIdAndId(
    @RequestParam storeHouseId: Long,
    @RequestParam autoPartId: Long,
  ): Mono<ResponseEntity<AutoPartWithPhotoResponse>> {
    return autoPartService.getByStoreHouseIdAndId(storeHouseId, autoPartId)
      .map { response -> ResponseEntity.ok(response) }
  }

  @GetMapping(params = ["telegramChatId", "autoPartId"])
  fun getByTelegramChatIdAndId(
    @RequestParam telegramChatId: Long,
    @RequestParam autoPartId: Long,
  ): Mono<ResponseEntity<AutoPartResponse>> {
    return autoPartService.getByTelegramChatIdAndId(telegramChatId, autoPartId)
      .map { response -> ResponseEntity.ok(response) }
  }

  @DeleteMapping
  fun deleteById(@RequestParam storeHouseId: Long, @RequestParam autoPartId: Long): Mono<ResponseEntity<Boolean>> {
    return autoPartService.deleteByStoreHouseIdAndId(storeHouseId, autoPartId)
      .map { response -> ResponseEntity.ok(response) }
  }
}
