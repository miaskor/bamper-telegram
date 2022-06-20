package by.miaskor.domain.api.domain

data class CarAutoPartRequest(
  override val storeHouseId: Long,
  val brandId: Long,
  val autoPartId: Long,
  override val limit: Long,
  override val offset: Long,
) : StoreHouseIdRequest(storeHouseId, limit, offset)
