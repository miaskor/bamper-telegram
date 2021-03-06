package by.miaskor.domain.controller

import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import by.miaskor.domain.api.domain.StoreHouseRequestWithConstraint
import by.miaskor.domain.service.AutoPartService
import by.miaskor.domain.service.StoreHouseConstraintHandler
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
  ): Mono<ResponseEntity<ResponseWithLimit<AutoPartResponse>>> {
    return autoPartService.getAllByStoreHouseId(storeHouseIdRequest)
      .map { ResponseEntity.ok(it) }
  }

  @PostMapping("/list/constraint")
  fun getAllByStoreHouseIdWithConstraint(
    @RequestBody storeHouseIdRequest: StoreHouseRequestWithConstraint,
  ): Mono<ResponseEntity<ResponseWithLimit<AutoPartResponse>>> {
    return storeHouseConstraintHandler.handle(storeHouseIdRequest)
      .map { ResponseEntity.ok(it) }
  }

  @GetMapping("/{storeHouseId}/{id}")
  fun getByStoreHouseIdAndId(
    @PathVariable storeHouseId: Long,
    @PathVariable id: Long,
  ): Mono<ResponseEntity<AutoPartResponse>> {
    return autoPartService.getByStoreHouseIdAndId(storeHouseId, id)
      .map { ResponseEntity.ok(it) }
  }

  @DeleteMapping("/{storeHouseId}/{id}")
  fun deleteById(@PathVariable storeHouseId: Long, @PathVariable id: Long): Mono<ResponseEntity<Boolean>> {
    return autoPartService.deleteByStoreHouseIdAndId(storeHouseId, id)
      .map { ResponseEntity.ok(it) }
  }
}
