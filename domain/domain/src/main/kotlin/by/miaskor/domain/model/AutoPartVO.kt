package by.miaskor.domain.model

data class AutoPartVO(
  val id: Long,
  val description: String,
  val photoPath: String,
  val price: Double,
  val quality: Boolean,
  val currency: String,
  val partNumber: String,
  val model: String,
  val brandName: String,
  val year: String,
  val autoPartEN: String,
  val autoPartRU: String,
)
