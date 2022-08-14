package by.miaskor.domain.model

data class AutoPartWithPhotoUrlVO(
  val description: String,
  val photoPath: String,
  val price: Double,
  val quality: Boolean,
  val currency: String,
  val partNumber: String,
  val model: String,
  val brandName: String,
  val year: String,
  val body: String,
  val transmission: String,
  val engineCapacity: Double,
  val fuelType: String,
  val engineType: String,
  val autoPartName: String,
)
