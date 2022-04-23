package by.miaskor.domain.repository

import java.io.Serializable

interface CrudRepository<E : Serializable> {
  fun save(entity: E)
  fun findById(id: Long): E
  fun deleteById(id: Long)
  fun update(entity: E)
}
