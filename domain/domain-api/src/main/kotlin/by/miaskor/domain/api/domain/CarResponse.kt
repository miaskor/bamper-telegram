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
)
