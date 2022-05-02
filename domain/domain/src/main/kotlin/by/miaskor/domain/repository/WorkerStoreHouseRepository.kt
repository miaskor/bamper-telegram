package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.WorkerStoreHouse
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface WorkerStoreHouseRepository : CrudRepository<WorkerStoreHouse>

class JooqWorkerStoreHouseRepository(
  private val dslContext: DSLContext
): WorkerStoreHouseRepository {
  override fun save(entity: WorkerStoreHouse): Mono<Unit> {
    TODO("Not yet implemented")
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
