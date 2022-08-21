package by.miaskor.domain.model

data class CarVO(
  val id: Long,
  val body: String,
  val transmission: String,
  val engineCapacity: Double,
  val fuelType: String,
  val engineType: String,
  val year: String,
  val model: String,
  val brandName: String,
)
