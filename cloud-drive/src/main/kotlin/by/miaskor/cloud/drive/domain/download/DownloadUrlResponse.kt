package by.miaskor.cloud.drive.domain.download

data class DownloadUrlResponse(
  val href: String,
  val method: String,
  val templated: Boolean
)
