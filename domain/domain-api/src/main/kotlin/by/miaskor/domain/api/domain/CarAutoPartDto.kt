package by.miaskor.domain.api.domain

data class CarAutoPartDto(
  override val storeHouseId: Long,
  val brand: String,
  val autoPart: String,
  val model: String,
  override val limit: Long,
  override val offset: Long,
) : StoreHouseIdWithLimitRequest(storeHouseId, limit, offset)
