package by.miaskor.domain.mapper

import org.jooq.Record

interface EntityMapper<F, T : Record> {

  fun map(from: F): T
}
