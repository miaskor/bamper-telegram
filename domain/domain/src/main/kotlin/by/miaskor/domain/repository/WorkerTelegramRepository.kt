package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.WorkerTelegram
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface WorkerTelegramRepository : CrudRepository<WorkerTelegram>

class JooqWorkerTelegramRepository(
  private val dslContext: DSLContext
) : WorkerTelegramRepository {
  override fun save(entity: WorkerTelegram): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun findById(id: Long): Mono<WorkerTelegram> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: WorkerTelegram): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
