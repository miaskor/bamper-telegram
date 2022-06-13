package by.miaskor.bot.service.handler.list

import by.miaskor.bot.domain.ListEntity
import by.miaskor.bot.domain.ListEntityType

class ListEntityHandlerRegistry(
  vararg listHandlers: ListHandler<ListEntity>
) {

  private val mapListHandlers = listHandlers.associateBy { it.listEntityType }

  fun lookup(listEntityType: ListEntityType): ListHandler<ListEntity> {
    return mapListHandlers[listEntityType]
      ?: throw IllegalArgumentException("List entity handler with type=$listEntityType didn't found")
  }
}