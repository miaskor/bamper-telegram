package by.miaskor.cloud.drive.domain

data class UploadFileRequest(
  val chatId: Long,
  val image: ByteArray,
  val isAdvertisement: Boolean,
  val entityId: Long
)
