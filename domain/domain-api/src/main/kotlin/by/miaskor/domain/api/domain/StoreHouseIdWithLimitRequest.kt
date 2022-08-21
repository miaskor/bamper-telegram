package by.miaskor.domain.api.domain

open class StoreHouseIdWithLimitRequest(
  open val storeHouseId: Long,
  open val limit: Long,
  open val offset: Long,
)
