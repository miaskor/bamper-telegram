package by.miaskor.domain

data class ImportAdvertisementDto(
  val autoPartId: Long,
  val storeHouseId: Long,
  val article: String = "",
  val salePercent: Int = 0,
  val active: Boolean = false,
)
