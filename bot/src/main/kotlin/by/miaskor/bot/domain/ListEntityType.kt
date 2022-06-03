package by.miaskor.bot.domain

import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_NEXT
import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_PREV
import by.miaskor.bot.domain.CallbackQuery.CARS_NEXT
import by.miaskor.bot.domain.CallbackQuery.CARS_PREV

enum class ListEntityType(private vararg val callbackQuery: CallbackQuery) {

  AUTO_PART(AUTO_PARTS_PREV, AUTO_PARTS_NEXT) {
    override fun next() = AUTO_PARTS_NEXT
    override fun prev() = AUTO_PARTS_PREV
  },
  CAR(CARS_NEXT, CARS_PREV) {
    override fun next() = CARS_NEXT
    override fun prev() = CARS_PREV
  };

  abstract fun next(): CallbackQuery
  abstract fun prev(): CallbackQuery

  companion object {
    fun getByCallbackQuery(callbackQuery: CallbackQuery): ListEntityType {
      return values().first { it.callbackQuery.contains(callbackQuery) }
    }
  }
}