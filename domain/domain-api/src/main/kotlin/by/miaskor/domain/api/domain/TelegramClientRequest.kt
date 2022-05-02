package by.miaskor.domain.api.domain

data class TelegramClientRequest(
  val chatId: String,
  val chatLanguage: String,
  val username: String,
  val bamperClientId: Long? = null
)
