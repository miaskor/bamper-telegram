package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.StoreHouse
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface StoreHouseRepository: CrudRepository<StoreHouse>


class JooqStoreHouseRepository(
  private val dslContext: DSLContext
):StoreHouseRepository {
  override fun save(entity: StoreHouse): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun findById(id: Long): Mono<StoreHouse> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: StoreHouse): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
