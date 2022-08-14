package by.miaskor.domain

data class ImportAdvertisementRequest(
  val autoPartId: Long,
  val telegramCharId: Long,
  val bamperSessionId: String,
  val article: String = "",
  val salePercent: Int = 0,
  val active: Boolean = false,
)
