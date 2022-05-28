package by.miaskor.domain.api.domain

data class ResponseWithLimit<T>(
  val entities: List<T>,
  val isMoreExists: Boolean = false
)
