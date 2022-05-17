package by.miaskor.bot.domain

data class CarDto(
  val brandName: String,
  val model: String,
  val year: String,
  val body: String,
  val transmission: String,
  val engineCapacity: Double,
  val fuelType: String,
  val engineType: String
)
