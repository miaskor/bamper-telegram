package by.miaskor.domain.api.domain

data class StoreHouseRequestWithConstraint(
  val constraint: String,
  val constraintType: ConstraintType,
  override val storeHouseId: Long,
  override val limit: Long,
  override val offset: Long,
) : StoreHouseIdRequest(storeHouseId, limit, offset)
