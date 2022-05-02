package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.TelegramClientStoreHouse
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface TelegramClientStoreHouseRepository: CrudRepository<TelegramClientStoreHouse>

class JooqTelegramClientStoreHouseRepository(
  private val dslContext: DSLContext
): TelegramClientStoreHouseRepository {
  override fun save(entity: TelegramClientStoreHouse): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun findById(id: Long): Mono<TelegramClientStoreHouse> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: TelegramClientStoreHouse): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
