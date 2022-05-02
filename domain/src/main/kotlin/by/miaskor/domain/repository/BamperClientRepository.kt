package by.miaskor.domain.repository

import by.miaskor.domain.mapper.BamperClientMapper
import by.miaskor.domain.tables.pojos.BamperClient
import by.miaskor.domain.tables.references.BAMPER_CLIENT
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface BamperClientRepository: CrudRepository<BamperClient>

class JooqBamperClientRepository(
  private val dslContext: DSLContext
): BamperClientRepository{
  override fun save(entity: BamperClient): Mono<Unit> {
    return Mono.just(entity)
      .flatMap(BamperClientMapper::map)
      .map { bamperClientRecord ->
        dslContext.insertInto(BAMPER_CLIENT)
          .set(bamperClientRecord)
          .executeAsync()
      }
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
