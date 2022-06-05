package by.miaskor.bot.service.handler.list

import by.miaskor.bot.domain.ListEntityType

class ListEntityHandlerRegistry(
  vararg listHandlers: ListHandler
) {

  private val mapListHandlers = listHandlers.associateBy { it.listEntityType }

  fun lookup(listEntityType: ListEntityType): ListHandler {
    return mapListHandlers[listEntityType]
      ?: throw IllegalArgumentException("List entity handler with type=$listEntityType didn't found")
  }
}