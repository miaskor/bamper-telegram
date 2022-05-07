package by.miaskor.bot.service

import by.miaskor.bot.domain.BotState
import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Mono

object BotStateChanger {

  lateinit var telegramClientCache: TelegramClientCache
  private val log = LogManager.getLogger()

  fun <T : Any> Mono<T>.changeBotState(chatId: () -> Long, botState: BotState): Mono<T> {
    return this.flatMap { t ->
      Mono.fromSupplier(chatId)
        .map { chatId.invoke() }
        .flatMap {
          telegramClientCache.getTelegramClient(it)
            .zipWith(Mono.just(it))
        }
        .doOnNext { log.info("Change botState for=$it") }
        .doOnNext {
          telegramClientCache.populate(
            it.t2,
            it.t1.copy(currentBotState = botState, previousBotState = it.t1.currentBotState)
          )
        }.then(Mono.just(t))
    }
  }
}
