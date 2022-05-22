package by.miaskor.domain.repository

import by.miaskor.domain.api.domain.WorkerTelegramDto
import by.miaskor.domain.tables.pojos.WorkerTelegram
import by.miaskor.domain.tables.references.WORKER_TELEGRAM
import org.jooq.DSLContext
import reactor.core.publisher.Mono

interface WorkerTelegramRepository : CrudRepository<WorkerTelegram> {
  fun find(workerChatId: Long, employerChatId: Long): Mono<WorkerTelegram>
  fun findAllWorkerChatIdByEmployerChatId(employerChatId: Long): Mono<List<Long>>
  fun remove(workerTelegramDto: WorkerTelegramDto): Mono<Unit>
}

class JooqWorkerTelegramRepository(
  private val dslContext: DSLContext
) : WorkerTelegramRepository {
  override fun find(workerChatId: Long, employerChatId: Long): Mono<WorkerTelegram> {
    return Mono.fromSupplier {
      dslContext.selectFrom(WORKER_TELEGRAM)
        .where(WORKER_TELEGRAM.WORKER_TELEGRAM_CHAT_ID.eq(workerChatId))
        .and(WORKER_TELEGRAM.EMPLOYER_TELEGRAM_CHAT_ID.eq(employerChatId))
        .fetchOneInto(WorkerTelegram::class.java)
    }
  }

  override fun findAllWorkerChatIdByEmployerChatId(employerChatId: Long): Mono<List<Long>> {
    return Mono.fromSupplier {
      dslContext.selectFrom(WORKER_TELEGRAM)
        .where(WORKER_TELEGRAM.EMPLOYER_TELEGRAM_CHAT_ID.eq(employerChatId))
        .fetch { it.workerTelegramChatId }
    }
  }

  override fun remove(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return Mono.fromSupplier {
      dslContext.deleteFrom(WORKER_TELEGRAM)
        .where(WORKER_TELEGRAM.EMPLOYER_TELEGRAM_CHAT_ID.eq(workerTelegramDto.employerChatId))
        .and(WORKER_TELEGRAM.WORKER_TELEGRAM_CHAT_ID.eq(workerTelegramDto.employeeChatId))
        .execute()
    }
  }

  override fun save(entity: WorkerTelegram): Mono<Unit> {
    return Mono.just(entity)
      .map { dslContext.newRecord(WORKER_TELEGRAM, it) }
      .map { workerTelegramRecord ->
        dslContext.insertInto(WORKER_TELEGRAM)
          .set(workerTelegramRecord)
          .onDuplicateKeyIgnore()
          .execute()
      }
  }

  override fun findById(id: Long): Mono<WorkerTelegram> {
    TODO("Not yet implemented")
  }

  override fun deleteById(id: Long): Mono<Unit> {
    TODO("Not yet implemented")
  }

  override fun update(entity: WorkerTelegram): Mono<Unit> {
    TODO("Not yet implemented")
  }
}
