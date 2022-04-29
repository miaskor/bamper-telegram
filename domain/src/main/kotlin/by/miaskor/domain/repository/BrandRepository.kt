package by.miaskor.domain.repository

import by.miaskor.domain.mapper.BrandMapper
import by.miaskor.domain.tables.pojos.Brand
import by.miaskor.domain.tables.references.BRAND
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface BrandRepository : CrudRepository<Brand>

class JooqBrandRepository(
  private val dslContext: DSLContext
) : BrandRepository {
  override fun save(entity: Brand): Mono<Unit> {
    return Mono.just(entity)
      .flatMap(BrandMapper::map)
      .map { brandRecord ->
        dslContext.insertInto(BRAND)
          .set(brandRecord)
          .executeAsync()
      }
  }

  override fun findById(id: Long): Mono<Brand> {
    return Mono.fromSupplier {
      dslContext.selectFrom(BRAND)
        .where(BRAND.ID.eq(id))
        .fetchOneInto(Brand::class.java) ?: Brand()
    }
  }

  override fun deleteById(id: Long): Mono<Unit> {
    return Mono.fromSupplier {
      dslContext.deleteFrom(BRAND)
        .where(BRAND.ID.eq(id))
        .executeAsync()
    }
  }

  override fun update(entity: Brand): Mono<Unit> {
    return Mono.just(entity)
      .filter { it.id != null }
      .flatMap(BrandMapper::map)
      .map { brandRecord ->
        dslContext.update(BRAND)
          .set(brandRecord)
          .where(BRAND.ID.eq(entity.id))
          .executeAsync()
      }
  }
}
