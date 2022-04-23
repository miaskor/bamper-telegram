package by.miaskor.domain.repository

import by.miaskor.domain.mapper.AdvertisementMapper
import by.miaskor.domain.tables.pojos.Advertisement
import by.miaskor.domain.tables.references.ADVERTISEMENT
import org.jooq.DSLContext

interface AdvertisementRepository : CrudRepository<Advertisement>

class JooqAdvertisementRepository(
  private val dslContext: DSLContext
) : AdvertisementRepository {
  override fun save(entity: Advertisement) {
    val advertisementRecord = AdvertisementMapper.map(entity)

    dslContext.insertInto(ADVERTISEMENT)
      .set(advertisementRecord)
      .executeAsync()
  }

  override fun findById(id: Long): Advertisement {
    return dslContext.selectFrom(ADVERTISEMENT)
      .where(ADVERTISEMENT.ID.eq(id))
      .fetchOneInto(Advertisement::class.java) ?: Advertisement()
  }

  override fun deleteById(id: Long) {
    dslContext.deleteFrom(ADVERTISEMENT)
      .where(ADVERTISEMENT.ID.eq(id))
      .executeAsync()
  }

  override fun update(entity: Advertisement) {
    if (entity.id == null) {
      return
    }
    val advertisementRecord = AdvertisementMapper.map(entity)
    dslContext.update(ADVERTISEMENT)
      .set(advertisementRecord)
      .where(ADVERTISEMENT.ID.eq(entity.id))
      .executeAsync()
  }
}
