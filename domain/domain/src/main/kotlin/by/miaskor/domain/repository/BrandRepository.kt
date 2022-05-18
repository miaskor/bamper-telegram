package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.Brand
import by.miaskor.domain.tables.references.BRAND
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface BrandRepository : CrudRepository<Brand> {
  fun findByBrandName(brandName: String): Mono<Brand>
  fun findByBrandNameAndModel(brandName: String, model: String): Mono<Brand>
}

class JooqBrandRepository(
  private val dslContext: DSLContext
) : BrandRepository {
  override fun findByBrandName(brandName: String): Mono<Brand> {
    return Mono.fromSupplier {
      dslContext.selectFrom(BRAND)
        .where(BRAND.BRAND_NAME.equalIgnoreCase(brandName))
        .fetchAnyInto(Brand::class.java)
    }
  }

  override fun findByBrandNameAndModel(brandName: String, model: String): Mono<Brand> {
    return Mono.fromSupplier {
      dslContext.selectFrom(BRAND)
        .where(BRAND.BRAND_NAME.equalIgnoreCase(brandName))
        .and(BRAND.MODEL.equalIgnoreCase(model))
        .fetchAnyInto(Brand::class.java)
    }
  }

  override fun save(entity: Brand): Mono<Unit> {
    return Mono.just(entity)
      .map { dslContext.newRecord(BRAND, it) }
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
        .fetchOneInto(Brand::class.java)
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
      .map { dslContext.newRecord(BRAND, it) }
      .map { brandRecord ->
        dslContext.update(BRAND)
          .set(brandRecord)
          .where(BRAND.ID.eq(entity.id))
          .executeAsync()
      }
  }
}
