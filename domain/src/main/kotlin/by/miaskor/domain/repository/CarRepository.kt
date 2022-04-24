package by.miaskor.domain.repository

import by.miaskor.domain.mapper.CarMapper
import by.miaskor.domain.tables.pojos.Car
import by.miaskor.domain.tables.references.CAR
import org.jooq.DSLContext

interface CarRepository : CrudRepository<Car>

class JooqCarRepository(
  private val dslContext: DSLContext
) : CarRepository {
  override fun save(entity: Car) {
    val carRecord = CarMapper.map(entity)

    dslContext.insertInto(CAR)
      .set(carRecord)
      .executeAsync()
  }

  override fun findById(id: Long): Car {
    return dslContext.selectFrom(CAR)
      .where(CAR.ID.eq(id))
      .fetchOneInto(Car::class.java) ?: Car()
  }

  override fun deleteById(id: Long) {
    dslContext.deleteFrom(CAR)
      .where(CAR.ID.eq(id))
      .executeAsync()
  }

  override fun update(entity: Car) {
    if (entity.id == null) {
      return
    }
    val carRecord = CarMapper.map(entity)
    dslContext.update(CAR)
      .set(carRecord)
      .where(CAR.ID.eq(entity.id))
      .executeAsync()
  }
}
