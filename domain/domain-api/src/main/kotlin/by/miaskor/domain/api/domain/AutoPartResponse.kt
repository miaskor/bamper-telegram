package by.miaskor.domain.api.domain

data class AutoPartResponse(
  val description: String,
  val photoDownloadUrl: String,
  val price: Double,
  val quality: Boolean,
  val currency: String,
  val partNumber: String,
  val model: String,
  val brand: String,
  val year: String,
  val body: String,
  val transmission: String,
  val engineCapacity: Double,
  val fuelType: String,
  val engineType: String,
  val autoPartName: String,
  val salePercent: Int = 0,
  val active: Boolean = false,
)
