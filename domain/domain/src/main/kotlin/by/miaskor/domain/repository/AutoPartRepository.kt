package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.AutoPart
import by.miaskor.domain.tables.references.AUTO_PART
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface AutoPartRepository : CrudRepository<AutoPart> {
  fun create(entity: AutoPart): Mono<Long>
  fun updatePhotoPathById(photoPath: String, id: Long): Mono<Unit>
}

class JooqAutoPartRepository(
  private val dslContext: DSLContext
) : AutoPartRepository {
  override fun save(entity: AutoPart): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun create(entity: AutoPart): Mono<Long> {
    return Mono.just(entity)
      .map { dslContext.newRecord(AUTO_PART, it) }
      .mapNotNull { autoPartRecord ->
        dslContext.insertInto(AUTO_PART)
          .set(autoPartRecord)
          .returning(AUTO_PART.ID)
          .fetchOne()
          ?.getValue(AUTO_PART.ID)
      }
  }

  override fun updatePhotoPathById(photoPath: String, id: Long): Mono<Unit> {
    return Mono.fromSupplier {
      dslContext.update(AUTO_PART)
        .set(AUTO_PART.PHOTO_PATH, photoPath)
        .where(AUTO_PART.ID.eq(id))
        .execute()
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
}
