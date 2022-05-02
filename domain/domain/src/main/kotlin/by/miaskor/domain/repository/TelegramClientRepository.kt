package by.miaskor.domain.repository

import by.miaskor.domain.mapper.CarMapper
import by.miaskor.domain.mapper.TelegramClientMapper
import by.miaskor.domain.tables.pojos.TelegramClient
import by.miaskor.domain.tables.references.CAR
import by.miaskor.domain.tables.references.TELEGRAM_CLIENT
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface TelegramClientRepository: CrudRepository<TelegramClient>


class JooqTelegramClientRepository(
  private val dslContext: DSLContext
): TelegramClientRepository {
  override fun save(entity: TelegramClient): Mono<Unit> {
    return Mono.just(entity)
      .flatMap(TelegramClientMapper::map)
      .map { telegramClientRecord ->
        dslContext.insertInto(TELEGRAM_CLIENT)
          .set(telegramClientRecord)
          .executeAsync()
      }
  }

  override fun findById(id: Long): Mono<TelegramClient> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: TelegramClient): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
