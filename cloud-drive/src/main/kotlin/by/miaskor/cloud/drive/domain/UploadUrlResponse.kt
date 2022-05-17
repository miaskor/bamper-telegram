package by.miaskor.cloud.drive.domain

data class UploadUrlResponse(
  val operationId: String = "",
  val href: String = "",
  val method: String = "",
  val templated: Boolean = false
)
