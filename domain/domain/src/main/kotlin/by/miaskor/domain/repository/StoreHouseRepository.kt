package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.StoreHouse
import by.miaskor.domain.tables.references.STORE_HOUSE
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface StoreHouseRepository : CrudRepository<StoreHouse> {
  fun findByNameAndTelegramChatId(name: String, chatId: Long): Mono<StoreHouse>
  fun findAllByChatId(chatId: Long): Mono<List<StoreHouse>>
  fun findAllByIds(ids: List<Long>): Mono<List<StoreHouse>>
}

class JooqStoreHouseRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : StoreHouseRepository {
  override fun findByNameAndTelegramChatId(name: String, chatId: Long): Mono<StoreHouse> {
    return Mono.fromCallable<StoreHouse> {
      dslContext.selectFrom(STORE_HOUSE)
        .where(STORE_HOUSE.TELEGRAM_CHAT_ID.eq(chatId))
        .and(STORE_HOUSE.STORE_HOUSE_NAME.eq(name))
        .fetchOneInto(StoreHouse::class.java)
    }.subscribeOn(scheduler)
  }

  override fun findAllByChatId(chatId: Long): Mono<List<StoreHouse>> {
    return Mono.fromCallable {
      dslContext.selectFrom(STORE_HOUSE)
        .where(STORE_HOUSE.TELEGRAM_CHAT_ID.eq(chatId))
        .fetchInto(StoreHouse::class.java)
    }.subscribeOn(scheduler)
  }

  override fun findAllByIds(ids: List<Long>): Mono<List<StoreHouse>> {
    return Mono.fromCallable {
      dslContext.select()
        .from(STORE_HOUSE)
        .where(STORE_HOUSE.ID.`in`(ids))
        .fetchInto(StoreHouse::class.java)
    }.subscribeOn(scheduler)
  }

  override fun save(entity: StoreHouse): Mono<Unit> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(STORE_HOUSE, it) }
      .map { storeHouseRecord ->
        dslContext.insertInto(STORE_HOUSE)
          .set(storeHouseRecord)
          .onDuplicateKeyIgnore()
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun findById(id: Long): Mono<StoreHouse> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: StoreHouse): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
