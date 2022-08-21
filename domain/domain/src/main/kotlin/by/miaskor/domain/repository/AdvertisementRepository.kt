package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.Advertisement
import by.miaskor.domain.tables.references.ADVERTISEMENT
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface AdvertisementRepository : CrudRepository<Advertisement>

class JooqAdvertisementRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : AdvertisementRepository {
  override fun save(entity: Advertisement): Mono<Unit> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(ADVERTISEMENT, it) }
      .map { advertisementRecord ->
        dslContext.insertInto(ADVERTISEMENT)
          .set(advertisementRecord)
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun findById(id: Long): Mono<Advertisement> {
    return Mono.fromCallable<Advertisement> {
      dslContext.selectFrom(ADVERTISEMENT)
        .where(ADVERTISEMENT.ID.eq(id))
        .fetchOneInto(Advertisement::class.java)
    }.subscribeOn(scheduler)
  }

  override fun deleteById(id: Long): Mono<Unit> {
    return Mono.fromCallable {
      dslContext.deleteFrom(ADVERTISEMENT)
        .where(ADVERTISEMENT.ID.eq(id))
        .execute()
    }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun update(entity: Advertisement): Mono<Unit> {
    return Mono.fromCallable { entity }
      .filter { it.id != null }
      .map { dslContext.newRecord(ADVERTISEMENT, it) }
      .map { advertisementRecord ->
        dslContext.update(ADVERTISEMENT)
          .set(advertisementRecord)
          .where(ADVERTISEMENT.ID.eq(entity.id))
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
  }
}
