package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.WorkerStoreHouse
import by.miaskor.domain.tables.references.WORKER_STORE_HOUSE
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface WorkerStoreHouseRepository : CrudRepository<WorkerStoreHouse>

class JooqWorkerStoreHouseRepository(
  private val dslContext: DSLContext
) : WorkerStoreHouseRepository {
  override fun save(entity: WorkerStoreHouse): Mono<Unit> {
    return Mono.just(entity)
      .map { dslContext.newRecord(WORKER_STORE_HOUSE, it) }
      .map { workerStoreHouseRecord ->
        dslContext.insertInto(WORKER_STORE_HOUSE)
          .set(workerStoreHouseRecord)
          .onDuplicateKeyUpdate()
          .set(WORKER_STORE_HOUSE.WORKER_PRIVILEGE, entity.workerPrivilege)
          .execute()
      }
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
