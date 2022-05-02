package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.CarPart
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface CarPartRepository: CrudRepository<CarPart>

class JooqCarPartRepository(
  private val dslContext: DSLContext
): CarPartRepository{
  override fun save(entity: CarPart): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun findById(id: Long): Mono<CarPart> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: CarPart): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
