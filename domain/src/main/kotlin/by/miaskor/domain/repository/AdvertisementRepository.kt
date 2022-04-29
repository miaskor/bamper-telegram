package by.miaskor.domain.repository

import by.miaskor.domain.mapper.AdvertisementMapper
import by.miaskor.domain.tables.pojos.Advertisement
import by.miaskor.domain.tables.references.ADVERTISEMENT
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface AdvertisementRepository : CrudRepository<Advertisement>

class JooqAdvertisementRepository(
  private val dslContext: DSLContext
) : AdvertisementRepository {
  override fun save(entity: Advertisement): Mono<Unit> {
    return Mono.just(entity)
      .flatMap(AdvertisementMapper::map)
      .map { advertisementRecord ->
        dslContext.insertInto(ADVERTISEMENT)
          .set(advertisementRecord)
          .executeAsync()
      }
  }

  override fun findById(id: Long): Mono<Advertisement> {
    return Mono.fromSupplier {
      dslContext.selectFrom(ADVERTISEMENT)
        .where(ADVERTISEMENT.ID.eq(id))
        .fetchOneInto(Advertisement::class.java) ?: Advertisement()
    }
  }

  override fun deleteById(id: Long): Mono<Unit> {
    return Mono.fromSupplier {
      dslContext.deleteFrom(ADVERTISEMENT)
        .where(ADVERTISEMENT.ID.eq(id))
        .executeAsync()
    }
  }

  override fun update(entity: Advertisement): Mono<Unit> {
    return Mono.just(entity)
      .filter { it.id != null }
      .flatMap(AdvertisementMapper::map)
      .map { advertisementRecord ->
        dslContext.update(ADVERTISEMENT)
          .set(advertisementRecord)
          .where(ADVERTISEMENT.ID.eq(entity.id))
          .executeAsync()
      }
  }
}
