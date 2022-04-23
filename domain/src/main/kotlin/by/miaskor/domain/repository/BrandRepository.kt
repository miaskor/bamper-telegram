package by.miaskor.domain.repository

import by.miaskor.domain.mapper.BrandMapper
import by.miaskor.domain.tables.pojos.Brand
import by.miaskor.domain.tables.references.BRAND
import org.jooq.DSLContext

interface BrandRepository : CrudRepository<Brand>

class JooqBrandRepository(
  private val dslContext: DSLContext
) : BrandRepository {
  override fun save(entity: Brand) {
    val brandRecord = BrandMapper.map(entity)

    dslContext.insertInto(BRAND)
      .set(brandRecord)
      .executeAsync()
  }

  override fun findById(id: Long): Brand {
    return dslContext.selectFrom(BRAND)
      .where(BRAND.ID.eq(id))
      .fetchOneInto(Brand::class.java) ?: Brand()
  }

  override fun deleteById(id: Long) {
    dslContext.deleteFrom(BRAND)
      .where(BRAND.ID.eq(id))
      .executeAsync()
  }

  override fun update(entity: Brand) {
    if (entity.id == null) {
      return
    }
    val brandRecord = BrandMapper.map(entity)
    dslContext.update(BRAND)
      .set(brandRecord)
      .where(BRAND.ID.eq(entity.id))
      .executeAsync()
  }
}
