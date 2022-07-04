package by.miaskor.domain.repository

import by.miaskor.domain.api.domain.CarAutoPartDto
import by.miaskor.domain.model.AutoPartVO
import by.miaskor.domain.tables.pojos.AutoPart
import by.miaskor.domain.tables.references.AUTO_PART
import by.miaskor.domain.tables.references.BRAND
import by.miaskor.domain.tables.references.CAR
import by.miaskor.domain.tables.references.CAR_PART
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface AutoPartRepository : CrudRepository<AutoPart> {
  fun findAllByStoreHouseId(storeHouseId: Long, limit: Long, offset: Long): Mono<List<AutoPartVO>>
  fun findAllByStoreHouseIdAndCarAndCarPart(carAutoPartDto: CarAutoPartDto): Mono<List<AutoPartVO>>
  fun findAllByStoreHouseIdAndPartNumber(
    partNumber: String,
    storeHouseId: Long,
    limit: Long,
    offset: Long,
  ): Mono<List<AutoPartVO>>

  fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Int>
  fun findByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<AutoPartVO>
}

class JooqAutoPartRepository(
  private val dslContext: DSLContext,
) : AutoPartRepository {
  override fun save(entity: AutoPart): Mono<Unit> {
    return Mono.just(entity)
      .map { dslContext.newRecord(AUTO_PART, it) }
      .map { autoPartRecord ->
        dslContext.insertInto(AUTO_PART)
          .set(autoPartRecord)
          .execute()
      }
  }

  override fun findAllByStoreHouseId(storeHouseId: Long, limit: Long, offset: Long): Mono<List<AutoPartVO>> {
    return Mono.fromSupplier {
      dslContext.select(autoPartColumns)
        .from(AUTO_PART)
        .join(CAR).on(AUTO_PART.CAR_ID.eq(CAR.ID))
        .join(BRAND).on(BRAND.ID.eq(CAR.BRAND_ID))
        .join(CAR_PART).on(AUTO_PART.CAR_PART_ID.eq(CAR_PART.ID))
        .where(AUTO_PART.STORE_HOUSE_ID.eq(storeHouseId))
        .limit(limit)
        .offset(offset)
        .fetchInto(AutoPartVO::class.java)
    }
  }

  override fun findAllByStoreHouseIdAndCarAndCarPart(carAutoPartDto: CarAutoPartDto): Mono<List<AutoPartVO>> {
    return Mono.fromSupplier {
      dslContext.select(autoPartColumns)
        .from(AUTO_PART)
        .join(CAR).on(AUTO_PART.CAR_ID.eq(CAR.ID))
        .join(BRAND).on(BRAND.ID.eq(CAR.BRAND_ID))
        .join(CAR_PART).on(AUTO_PART.CAR_PART_ID.eq(CAR_PART.ID))
        .where(AUTO_PART.STORE_HOUSE_ID.eq(carAutoPartDto.storeHouseId))
        .and(CAR_PART.NAME_EN.eq(carAutoPartDto.autoPart))
        .or(CAR_PART.NAME_RU.eq(carAutoPartDto.autoPart))
        .and(BRAND.BRAND_NAME.eq(carAutoPartDto.brand))
        .and(BRAND.MODEL.eq(carAutoPartDto.model))
        .limit(carAutoPartDto.limit)
        .offset(carAutoPartDto.offset)
        .fetchInto(AutoPartVO::class.java)
    }
  }

  override fun findAllByStoreHouseIdAndPartNumber(
    partNumber: String,
    storeHouseId: Long,
    limit: Long,
    offset: Long,
  ): Mono<List<AutoPartVO>> {
    return Mono.fromSupplier {
      dslContext.select(autoPartColumns)
        .from(AUTO_PART)
        .join(CAR).on(AUTO_PART.CAR_ID.eq(CAR.ID))
        .join(BRAND).on(BRAND.ID.eq(CAR.BRAND_ID))
        .join(CAR_PART).on(AUTO_PART.CAR_PART_ID.eq(CAR_PART.ID))
        .where(AUTO_PART.STORE_HOUSE_ID.eq(storeHouseId))
        .and(AUTO_PART.PART_NUMBER.eq(partNumber))
        .limit(limit)
        .offset(offset)
        .fetchInto(AutoPartVO::class.java)
    }
  }

  override fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Int> {
    return Mono.fromSupplier {
      dslContext.deleteFrom(AUTO_PART)
        .where(AUTO_PART.ID.eq(id))
        .and(AUTO_PART.STORE_HOUSE_ID.eq(storeHouseId))
        .execute()
    }
  }

  override fun findByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<AutoPartVO> {
    return Mono.fromSupplier {
      dslContext.select(autoPartColumns)
        .from(AUTO_PART)
        .join(CAR).on(AUTO_PART.CAR_ID.eq(CAR.ID))
        .join(BRAND).on(BRAND.ID.eq(CAR.BRAND_ID))
        .join(CAR_PART).on(AUTO_PART.CAR_PART_ID.eq(CAR_PART.ID))
        .where(AUTO_PART.STORE_HOUSE_ID.eq(storeHouseId))
        .and(AUTO_PART.ID.eq(id))
        .fetchOneInto(AutoPartVO::class.java)
    }
  }

  override fun findById(id: Long): Mono<AutoPart> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: AutoPart): Mono<Unit> {
    TODO("Not yet implemented")
  }

  private companion object {
    private val autoPartColumns = listOf(
      AUTO_PART.ID.`as`("id"),
      AUTO_PART.DESCRIPTION.`as`("description"),
      AUTO_PART.PHOTO_PATH.`as`("photoPath"),
      AUTO_PART.PRICE.`as`("price"),
      AUTO_PART.QUALITY.`as`("quality"),
      AUTO_PART.CURRENCY.`as`("currency"),
      AUTO_PART.PART_NUMBER.`as`("partNumber"),
      BRAND.MODEL.`as`("model"),
      BRAND.BRAND_NAME.`as`("brandName"),
      CAR.YEAR.`as`("year"),
      CAR_PART.NAME_EN.`as`("autoPartEN"),
      CAR_PART.NAME_RU.`as`("autoPartRU")
    )
  }
}
