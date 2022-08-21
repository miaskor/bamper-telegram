package by.miaskor.domain.repository

import by.miaskor.domain.model.CarVO
import by.miaskor.domain.tables.pojos.Car
import by.miaskor.domain.tables.references.BRAND
import by.miaskor.domain.tables.references.CAR
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface CarRepository : CrudRepository<Car> {
  fun create(entity: Car): Mono<Long>
  fun findByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Car>
  fun findAllByStoreHouseId(storeHouseId: Long, limit: Long, offset: Long): Mono<List<CarVO>>
  fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Int>
}

class JooqCarRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : CarRepository {
  override fun save(entity: Car): Mono<Unit> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(CAR, it) }
      .map { carRecord ->
        dslContext.insertInto(CAR)
          .set(carRecord)
          .executeAsync()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun create(entity: Car): Mono<Long> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(CAR, it) }
      .mapNotNull<Long> { carRecord ->
        dslContext.insertInto(CAR)
          .set(carRecord)
          .returning(CAR.ID)
          .fetchOne()
          ?.getValue(CAR.ID)
      }.subscribeOn(scheduler)
  }

  override fun findByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Car> {
    return Mono.fromCallable<Car> {
      dslContext.selectFrom(CAR)
        .where(CAR.ID.eq(id))
        .and(CAR.STORE_HOUSE_ID.eq(storeHouseId))
        .fetchOneInto(Car::class.java)
    }.subscribeOn(scheduler)
  }

  override fun findAllByStoreHouseId(storeHouseId: Long, limit: Long, offset: Long): Mono<List<CarVO>> {
    return Mono.fromCallable {
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
        .limit(limit)
        .offset(offset)
        .fetchInto(CarVO::class.java)
    }.subscribeOn(scheduler)
  }

  override fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Int> {
    return Mono.fromCallable {
      dslContext.deleteFrom(CAR)
        .where(CAR.ID.eq(id))
        .and(CAR.STORE_HOUSE_ID.eq(storeHouseId))
        .execute()
    }.subscribeOn(scheduler)
  }

  override fun findById(id: Long): Mono<Car> {
    return Mono.fromCallable<Car> {
      dslContext.selectFrom(CAR)
        .where(CAR.ID.eq(id))
        .fetchOneInto(Car::class.java)
    }.subscribeOn(scheduler)
  }

  override fun deleteById(id: Long): Mono<Unit> {
    return Mono.fromCallable {
      dslContext.deleteFrom(CAR)
        .where(CAR.ID.eq(id))
        .execute()
    }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun update(entity: Car): Mono<Unit> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(CAR, it) }
      .map { carRecord ->
        dslContext.update(CAR)
          .set(carRecord)
          .where(CAR.ID.eq(entity.id))
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }
}
