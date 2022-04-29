package by.miaskor.domain.repository

import by.miaskor.domain.mapper.CarMapper
import by.miaskor.domain.tables.pojos.Car
import by.miaskor.domain.tables.references.CAR
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface CarRepository : CrudRepository<Car>

class JooqCarRepository(
  private val dslContext: DSLContext
) : CarRepository {
  override fun save(entity: Car): Mono<Unit> {
    return Mono.just(entity)
      .flatMap(CarMapper::map)
      .map { carRecord ->
        dslContext.insertInto(CAR)
          .set(carRecord)
          .executeAsync()
      }
  }

  override fun findById(id: Long): Mono<Car> {
    return Mono.fromSupplier {
      dslContext.selectFrom(CAR)
        .where(CAR.ID.eq(id))
        .fetchOneInto(Car::class.java) ?: Car()
    }
  }

  override fun deleteById(id: Long): Mono<Unit> {
    return Mono.fromSupplier {
      dslContext.deleteFrom(CAR)
        .where(CAR.ID.eq(id))
        .executeAsync()
    }
  }

  override fun update(entity: Car): Mono<Unit> {
    return Mono.just(entity)
      .flatMap(CarMapper::map)
      .map { carRecord ->
        dslContext.update(CAR)
          .set(carRecord)
          .where(CAR.ID.eq(entity.id))
          .executeAsync()
      }
  }
}
