package by.miaskor.domain.repository

import by.miaskor.domain.model.WorkerStoreHouseVO
import by.miaskor.domain.tables.pojos.WorkerStoreHouse
import by.miaskor.domain.tables.references.WORKER_STORE_HOUSE
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface WorkerStoreHouseRepository : CrudRepository<WorkerStoreHouse> {
  fun findStoreHousesByChatId(chatId: Long): Mono<List<WorkerStoreHouseVO>>
  fun removeByChatId(chatId: Long): Mono<Unit>
}

class JooqWorkerStoreHouseRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : WorkerStoreHouseRepository {
  override fun findStoreHousesByChatId(chatId: Long): Mono<List<WorkerStoreHouseVO>> {
    return Mono.fromCallable {
      dslContext.select(
        WORKER_STORE_HOUSE.STORE_HOUSE_ID.`as`("storeHouseId"),
        WORKER_STORE_HOUSE.WORKER_PRIVILEGE.`as`("privilege")
      )
        .from(WORKER_STORE_HOUSE)
        .where(WORKER_STORE_HOUSE.WORKER_TELEGRAM_CLIENT_ID.eq(chatId))
        .fetchInto(WorkerStoreHouseVO::class.java)
    }.subscribeOn(scheduler)
  }

  override fun removeByChatId(chatId: Long): Mono<Unit> {
    return Mono.fromCallable {
      dslContext.deleteFrom(WORKER_STORE_HOUSE)
        .where(WORKER_STORE_HOUSE.WORKER_TELEGRAM_CLIENT_ID.eq(chatId))
        .execute()
    }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun save(entity: WorkerStoreHouse): Mono<Unit> {
    return Mono.fromCallable{entity}
      .map { dslContext.newRecord(WORKER_STORE_HOUSE, it) }
      .map { workerStoreHouseRecord ->
        dslContext.insertInto(WORKER_STORE_HOUSE)
          .set(workerStoreHouseRecord)
          .onDuplicateKeyUpdate()
          .set(WORKER_STORE_HOUSE.WORKER_PRIVILEGE, entity.workerPrivilege)
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun findById(id: Long): Mono<WorkerStoreHouse> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: WorkerStoreHouse): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
