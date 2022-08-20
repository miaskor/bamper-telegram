package by.miaskor.domain.service.mapper

import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.model.CarVO

class CarMapper {

  fun map(listCarVO: List<CarVO>): List<CarResponse> {
    return listCarVO.map { carVO -> map(carVO) }
  }

  fun map(carVO: CarVO): CarResponse {
    return CarResponse(
      id = carVO.id,
      body = carVO.body,
      brandName = carVO.brandName,
      transmission = carVO.transmission,
      engineCapacity = carVO.engineCapacity,
      engineType = carVO.engineType,
      fuelType = carVO.fuelType,
      year = carVO.year,
      model = carVO.model,
    )
  }
}
