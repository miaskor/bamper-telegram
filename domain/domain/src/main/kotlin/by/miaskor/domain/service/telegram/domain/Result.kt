package by.miaskor.domain.service.telegram.domain

data class Result(
  val file_id: String,
  val file_unique_id: String,
  val file_size: Long,
  val file_path: String
)
