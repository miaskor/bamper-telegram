package by.miaskor.domain.api.domain

data class AutoPartDto(
  val description: String,
  val photoId: String,
  val price: Double,
  val quality: Boolean,
  val currency: String,
  val partNumber: String,
  val carId: Long,
  val carPartId: Long,
  val chatId: Long,
  val storeHouseId: Long
)
