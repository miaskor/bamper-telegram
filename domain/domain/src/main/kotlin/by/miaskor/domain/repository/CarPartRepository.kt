package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.CarPart
import by.miaskor.domain.tables.references.CAR_PART
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface CarPartRepository : CrudRepository<CarPart> {
  fun findIdByName(name: String): Mono<CarPart>
}

class JooqCarPartRepository(
  private val dslContext: DSLContext
) : CarPartRepository {
  override fun findIdByName(name: String): Mono<CarPart> {
    return Mono.fromSupplier {
      dslContext.selectFrom(CAR_PART)
        .where(CAR_PART.NAME_RU.eq(name))
        .or(CAR_PART.NAME_EN.eq(name))
        .fetchOneInto(CarPart::class.java)
    }
  }

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
