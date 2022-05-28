package by.miaskor.domain.api.domain

data class CarResponse(
  val id: Long,
  val body: String = "",
  val transmission: String = "",
  val engineCapacity: Double = 0.0,
  val fuelType: String = "",
  val engineType: String = "",
  val year: String = "",
  val model: String = "",
  val brandName: String = ""
) {
  companion object {
    fun disassembly(carResponse: CarResponse): Array<String> {
      return carResponse.let {
        arrayOf(
          it.id.toString(),
          it.brandName,
          it.model,
          it.year,
          it.body,
          it.transmission,
          it.engineCapacity.toString(),
          it.fuelType,
          it.engineType
        )
      }
    }
  }
}
