package by.miaskor.bot.domain

import by.miaskor.bot.domain.BotState.MAIN_MENU
import java.util.*

data class TelegramClient(
  val chatId: Long,
  val chatLanguage: String = "",
  val bamperClientId: Long? = null,
  val currentBotState: BotState,
  val previousBotStates: SortedSet<BotState> = sortedSetOf(MAIN_MENU)
)
