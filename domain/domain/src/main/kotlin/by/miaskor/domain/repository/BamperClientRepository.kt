package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.BamperClient
import by.miaskor.domain.tables.references.BAMPER_CLIENT
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface BamperClientRepository : CrudRepository<BamperClient>

class JooqBamperClientRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : BamperClientRepository {
  override fun save(entity: BamperClient): Mono<Unit> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(BAMPER_CLIENT, it) }
      .map { bamperClientRecord ->
        dslContext.insertInto(BAMPER_CLIENT)
          .set(bamperClientRecord)
          .onDuplicateKeyIgnore()
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun findById(id: Long): Mono<BamperClient> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: BamperClient): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
