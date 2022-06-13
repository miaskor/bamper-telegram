package by.miaskor.domain.api.domain

data class StoreHouseRequestWithConstraint(
  val constraint: String,
  val storeHouseId: Long,
  val limit: Long,
  val offset: Long
)
