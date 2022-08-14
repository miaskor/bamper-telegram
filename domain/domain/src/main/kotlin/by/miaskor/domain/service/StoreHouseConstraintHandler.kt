package by.miaskor.domain.service

import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.CarAutoPartDto
import by.miaskor.domain.api.domain.ConstraintType.CAR_AND_CAR_PART
import by.miaskor.domain.api.domain.ConstraintType.PART_NUMBER
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseRequestWithConstraint
import reactor.core.publisher.Mono

class StoreHouseConstraintHandler(
  private val autoPartService: AutoPartService,
) {

  fun handle(
    storeHouseRequestWithConstraint: StoreHouseRequestWithConstraint,
  ): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return when (storeHouseRequestWithConstraint.constraintType) {
      CAR_AND_CAR_PART -> {
        val carAutoPartDto = disassembleCarAndCarPartConstraint(storeHouseRequestWithConstraint)
        autoPartService.getAllByStoreHouseIdAndCarAndCarPart(carAutoPartDto)
      }

      PART_NUMBER -> autoPartService.getAllByStoreHouseIdAndPartNumber(storeHouseRequestWithConstraint)
    }
  }

  private companion object {
    private fun disassembleCarAndCarPartConstraint(request: StoreHouseRequestWithConstraint): CarAutoPartDto {
      val (brand, model, autoPart) = request.constraint.split(", ")

      return CarAutoPartDto(
        storeHouseId = request.storeHouseId,
        brand = brand,
        autoPart = autoPart,
        model = model,
        limit = request.limit,
        offset = request.offset
      )
    }
  }
}
