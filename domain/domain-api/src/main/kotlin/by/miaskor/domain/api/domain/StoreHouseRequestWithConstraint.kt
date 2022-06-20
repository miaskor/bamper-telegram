package by.miaskor.domain.api.domain

data class StoreHouseRequestWithConstraint(
  val constraint: String,
  override val storeHouseId: Long,
  override val limit: Long,
  override val offset: Long,
) : StoreHouseIdRequest(storeHouseId, limit, offset)
