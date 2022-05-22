package by.miaskor.domain.repository

import reactor.core.publisher.Mono
import java.io.Serializable

interface CrudRepository<E : Serializable> {
  fun save(entity: E): Mono<Unit>
  fun findById(id: Long): Mono<E>
  fun deleteById(id: Long): Mono<Unit>
  fun update(entity: E): Mono<Unit>
}
