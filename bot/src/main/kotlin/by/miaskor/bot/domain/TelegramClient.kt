package by.miaskor.bot.domain

data class TelegramClient(
  val chatId: Long,
  val chatLanguage: String = "",
  val bamperClientId: Long? = null,
  val botState: BotState
)
