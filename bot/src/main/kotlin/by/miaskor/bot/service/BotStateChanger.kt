package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Mono

object BotStateChanger {

  lateinit var telegramClientCache: TelegramClientCache
  private val log = LogManager.getLogger()

  fun <T : Any> Mono<T>.changeBotState(chatId: () -> Long, botState: BotState, isBackCommand: Boolean = false): Mono<T> {
    return this.flatMap { t ->
      Mono.fromSupplier{chatId.invoke()}
        .flatMap (telegramClientCache::getTelegramClient)
        .doOnNext { log.info("Change botState for=$it") }
        .doOnNext {
          telegramClientCache.populate(
            it.chatId,
            it.copy(currentBotState = botState).apply {
              if (!isBackCommand) {
                this.previousBotStates.add(it.currentBotState)
              }
            }
          )
        }.then(Mono.just(t))
    }
  }
}
