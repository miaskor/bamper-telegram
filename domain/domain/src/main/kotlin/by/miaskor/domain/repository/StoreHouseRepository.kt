package by.miaskor.domain.repository

import by.miaskor.domain.tables.pojos.StoreHouse
import by.miaskor.domain.tables.references.STORE_HOUSE
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface StoreHouseRepository : CrudRepository<StoreHouse> {
  fun findByNameAndTelegramChatId(name: String, chatId: Long): Mono<StoreHouse>
}

class JooqStoreHouseRepository(
  private val dslContext: DSLContext
) : StoreHouseRepository {
  override fun findByNameAndTelegramChatId(name: String, chatId: Long): Mono<StoreHouse> {
    return Mono.fromSupplier {
      dslContext.selectFrom(STORE_HOUSE)
        .where(STORE_HOUSE.TELEGRAM_CHAT_ID.eq(chatId))
        .and(STORE_HOUSE.STORE_HOUSE_NAME.eq(name))
        .fetchOneInto(StoreHouse::class.java)
    }
  }

  override fun save(entity: StoreHouse): Mono<Unit> {
    return Mono.just(entity)
      .map { dslContext.newRecord(STORE_HOUSE, it) }
      .map { storeHouseRecord ->
        dslContext.insertInto(STORE_HOUSE)
          .set(storeHouseRecord)
          .onDuplicateKeyIgnore()
          .execute()
      }
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
