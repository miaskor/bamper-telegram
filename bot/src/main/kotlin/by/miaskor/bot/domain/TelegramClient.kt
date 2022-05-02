package by.miaskor.bot.domain

data class TelegramClient(
  val chatId: Long,
  val username: String,
  val chatLanguage: String,
  val bamperClientId: Long? = null
)
