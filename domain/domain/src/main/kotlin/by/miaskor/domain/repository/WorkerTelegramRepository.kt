package by.miaskor.domain.repository

import by.miaskor.domain.api.domain.WorkerTelegramDto
import by.miaskor.domain.tables.pojos.WorkerTelegram
import by.miaskor.domain.tables.references.WORKER_TELEGRAM
import org.jooq.DSLContext
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

interface WorkerTelegramRepository : CrudRepository<WorkerTelegram> {
  fun find(workerChatId: Long, employerChatId: Long): Mono<WorkerTelegram>
  fun remove(workerTelegramDto: WorkerTelegramDto): Mono<Unit>
}

class JooqWorkerTelegramRepository(
  private val dslContext: DSLContext,
  private val scheduler: Scheduler = Schedulers.boundedElastic(),
) : WorkerTelegramRepository {
  override fun find(workerChatId: Long, employerChatId: Long): Mono<WorkerTelegram> {
    return Mono.fromCallable<WorkerTelegram> {
      dslContext.selectFrom(WORKER_TELEGRAM)
        .where(WORKER_TELEGRAM.WORKER_TELEGRAM_CHAT_ID.eq(workerChatId))
        .and(WORKER_TELEGRAM.EMPLOYER_TELEGRAM_CHAT_ID.eq(employerChatId))
        .fetchOneInto(WorkerTelegram::class.java)
    }.subscribeOn(scheduler)
  }

  override fun remove(workerTelegramDto: WorkerTelegramDto): Mono<Unit> {
    return Mono.fromCallable {
      dslContext.deleteFrom(WORKER_TELEGRAM)
        .where(WORKER_TELEGRAM.EMPLOYER_TELEGRAM_CHAT_ID.eq(workerTelegramDto.employerChatId))
        .and(WORKER_TELEGRAM.WORKER_TELEGRAM_CHAT_ID.eq(workerTelegramDto.employeeChatId))
        .execute()
    }.subscribeOn(scheduler)
      .then(Mono.empty())
  }

  override fun save(entity: WorkerTelegram): Mono<Unit> {
    return Mono.fromCallable { entity }
      .map { dslContext.newRecord(WORKER_TELEGRAM, it) }
      .map { workerTelegramRecord ->
        dslContext.insertInto(WORKER_TELEGRAM)
          .set(workerTelegramRecord)
          .onDuplicateKeyIgnore()
          .execute()
      }.subscribeOn(scheduler)
      .then(Mono.empty())
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
