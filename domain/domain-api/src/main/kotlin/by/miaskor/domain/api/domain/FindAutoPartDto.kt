package by.miaskor.domain.api.domain

data class FindAutoPartDto(
  val partNumber: String = "",
  val model: String = "",
  val brand: String = "",
  val year: String = "",
  val autoPart: String = "",
  val storeHouseId: Long
)