package by.miaskor.domain.api.domain

data class StoreHouseIdWithConstraint(
  val constraint: String,
  val constraintType: ConstraintType,
  override val storeHouseId: Long,
  override val limit: Long,
  override val offset: Long,
) : StoreHouseIdWithLimitRequest(storeHouseId, limit, offset)
