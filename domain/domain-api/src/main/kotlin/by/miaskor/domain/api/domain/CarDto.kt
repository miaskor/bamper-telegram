package by.miaskor.domain.api.domain

data class CarDto(
  val brandId: Long,
  val storeHouseName: String,
  val model: String,
  val year: String,
  val body: String,
  val transmission: String,
  val engineCapacity: Double,
  val fuelType: String,
  val engineType: String,
  val chatId: Long
)
