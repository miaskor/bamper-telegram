package by.miaskor.domain.api.domain

data class TelegramClientResponse(
  val chatId: Long = -1,
  val chatLanguage: String = "",
  val bamperClientId: Long? = null
)
