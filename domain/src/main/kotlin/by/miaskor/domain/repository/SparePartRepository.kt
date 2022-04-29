package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.SparePart
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface SparePartRepository : CrudRepository<SparePart>

class JooqSparePartRepository(
  private val dslContext: DSLContext
) : SparePartRepository {
  override fun save(entity: SparePart): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun findById(id: Long): Mono<SparePart> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: SparePart): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
