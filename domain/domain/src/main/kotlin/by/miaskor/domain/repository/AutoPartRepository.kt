package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.AutoPart
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface AutoPartRepository : CrudRepository<AutoPart>

class JooqAutoPartRepository(
  private val dslContext: DSLContext
) : AutoPartRepository {
  override fun save(entity: AutoPart): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun findById(id: Long): Mono<AutoPart> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: AutoPart): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
