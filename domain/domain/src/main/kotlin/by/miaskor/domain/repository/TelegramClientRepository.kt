package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.TelegramClient
import by.miaskor.domain.tables.references.TELEGRAM_CLIENT
import by.miaskor.domain.tables.references.WORKER_TELEGRAM
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface TelegramClientRepository : CrudRepository<TelegramClient> {
  fun findByChatId(chatId: Long): Mono<TelegramClient>
  fun findEmployeesByEmployerId(employerChatId: Long): Mono<List<TelegramClient>>
  fun findByUsername(username: String): Mono<TelegramClient>
}

class JooqTelegramClientRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : TelegramClientRepository {
  override fun save(entity: TelegramClient): Mono<Unit> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(TELEGRAM_CLIENT, it) }
      .map { telegramClientRecord ->
        dslContext.insertInto(TELEGRAM_CLIENT)
          .set(telegramClientRecord)
          .onDuplicateKeyUpdate()
          .set(TELEGRAM_CLIENT.CHAT_LANGUAGE, telegramClientRecord.chatLanguage)
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun findByChatId(chatId: Long): Mono<TelegramClient> {
    return Mono.fromCallable<TelegramClient> {
      dslContext.selectFrom(TELEGRAM_CLIENT)
        .where(TELEGRAM_CLIENT.CHAT_ID.eq(chatId))
        .fetchOneInto(TelegramClient::class.java)
    }.subscribeOn(scheduler)
  }

  override fun findEmployeesByEmployerId(employerChatId: Long): Mono<List<TelegramClient>> {
    return Mono.fromCallable {
      dslContext.select()
        .from(TELEGRAM_CLIENT)
        .join(WORKER_TELEGRAM).on(WORKER_TELEGRAM.WORKER_TELEGRAM_CHAT_ID.eq(TELEGRAM_CLIENT.CHAT_ID))
        .where(WORKER_TELEGRAM.EMPLOYER_TELEGRAM_CHAT_ID.eq(employerChatId))
        .fetch()
        .into((TelegramClient::class.java))
    }.subscribeOn(scheduler)
  }

  override fun findByUsername(username: String): Mono<TelegramClient> {
    return Mono.fromCallable<TelegramClient> {
      dslContext.selectFrom(TELEGRAM_CLIENT)
        .where(TELEGRAM_CLIENT.USER_NAME.eq(username))
        .fetchOneInto(TelegramClient::class.java)
    }.subscribeOn(scheduler)
  }

  override fun findById(id: Long): Mono<TelegramClient> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: TelegramClient): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
