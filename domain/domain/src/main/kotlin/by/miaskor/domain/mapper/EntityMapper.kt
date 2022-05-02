package by.miaskor.domain.mapper

import org.jooq.Record
import reactor.core.publisher.Mono

interface EntityMapper<F, T : Record> {

  fun map(from: F): Mono<T>
}

