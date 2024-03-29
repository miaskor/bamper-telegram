package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.CarPart
import by.miaskor.domain.tables.references.CAR_PART
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface CarPartRepository : CrudRepository<CarPart> {
  fun findIdByName(name: String): Mono<CarPart>
}

class JooqCarPartRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : CarPartRepository {
  override fun findIdByName(name: String): Mono<CarPart> {
    return Mono.fromCallable<CarPart> {
      dslContext.selectFrom(CAR_PART)
        .where(CAR_PART.NAME_RU.eq(name))
        .or(CAR_PART.NAME_EN.eq(name))
        .fetchOneInto(CarPart::class.java)
    }.subscribeOn(scheduler)
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
