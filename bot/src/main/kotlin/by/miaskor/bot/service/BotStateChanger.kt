package by.miaskor.bot.service

import org.apache.logging.log4j.LogManager
import reactor.core.publisher.Mono

object BotStateChanger {

  lateinit var telegramClientCache: TelegramClientCache
  private val log = LogManager.getLogger()

  fun <T> Mono<T>.changeBotState(chatId: () -> Long): Mono<Unit> {
    return this.flatMap {
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
            it.t1.copy(botState = it.t1.botState.next())
          )
        }.then(Mono.empty())
    }
  }
}
