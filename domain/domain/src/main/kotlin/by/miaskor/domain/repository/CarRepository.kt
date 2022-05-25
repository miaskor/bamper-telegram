package by.miaskor.domain.repository

import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.tables.pojos.Car
import by.miaskor.domain.tables.references.BRAND
import by.miaskor.domain.tables.references.CAR
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface CarRepository : CrudRepository<Car> {
  fun create(entity: Car): Mono<Long>
  fun findByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Car>
  fun findAllByStoreHouseId(storeHouseId: Long): Mono<List<CarResponse>>
}

class JooqCarRepository(
  private val dslContext: DSLContext
) : CarRepository {
  override fun save(entity: Car): Mono<Unit> {
    return Mono.just(entity)
      .map { dslContext.newRecord(CAR, it) }
      .map { carRecord ->
        dslContext.insertInto(CAR)
          .set(carRecord)
          .executeAsync()
      }
  }

  override fun create(entity: Car): Mono<Long> {
    return Mono.just(entity)
      .map { dslContext.newRecord(CAR, it) }
      .mapNotNull { carRecord ->
        dslContext.insertInto(CAR)
          .set(carRecord)
          .returning(CAR.ID)
          .fetchOne()
          ?.getValue(CAR.ID)
      }
  }

  override fun findByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Car> {
    return Mono.fromSupplier {
      dslContext.selectFrom(CAR)
        .where(CAR.ID.eq(id))
        .and(CAR.STORE_HOUSE_ID.eq(storeHouseId))
        .fetchOneInto(Car::class.java)
    }
  }

  override fun findAllByStoreHouseId(storeHouseId: Long): Mono<List<CarResponse>> {
    return Mono.fromSupplier {
      dslContext.select(
        CAR.ID,
        CAR.BODY,
        CAR.TRANSMISSION,
        CAR.ENGINE_CAPACITY,
        CAR.FUEL_TYPE,
        CAR.ENGINE_TYPE,
        CAR.YEAR,
        BRAND.BRAND_NAME,
        BRAND.MODEL
      )
        .from(CAR)
        .join(BRAND).on(BRAND.ID.eq(CAR.BRAND_ID))
        .where(CAR.STORE_HOUSE_ID.eq(storeHouseId))
        .fetchInto(CarResponse::class.java)
    }
  }

  override fun findById(id: Long): Mono<Car> {
    return Mono.fromSupplier {
      dslContext.selectFrom(CAR)
        .where(CAR.ID.eq(id))
        .fetchOneInto(Car::class.java)
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
      .map { dslContext.newRecord(CAR, it) }
      .map { carRecord ->
        dslContext.update(CAR)
          .set(carRecord)
          .where(CAR.ID.eq(entity.id))
          .executeAsync()
      }
  }
}
