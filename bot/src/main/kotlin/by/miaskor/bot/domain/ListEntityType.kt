package by.miaskor.bot.domain

import by.miaskor.bot.domain.CallbackCommand.AUTO_PARTS_NEXT
import by.miaskor.bot.domain.CallbackCommand.AUTO_PARTS_PREV
import by.miaskor.bot.domain.CallbackCommand.CARS_NEXT
import by.miaskor.bot.domain.CallbackCommand.CARS_PREV

enum class ListEntityType(private vararg val callbackCommand: CallbackCommand) {

  AUTO_PART(AUTO_PARTS_PREV, AUTO_PARTS_NEXT) {
    override fun next() = AUTO_PARTS_NEXT
    override fun prev() = AUTO_PARTS_PREV
  },
  CAR(CARS_NEXT, CARS_PREV) {
    override fun next() = CARS_NEXT
    override fun prev() = CARS_PREV
  };

  abstract fun next(): CallbackCommand
  abstract fun prev(): CallbackCommand

  companion object {
    fun getByCallbackCommand(callbackCommand: CallbackCommand): ListEntityType {
      return values().first { it.callbackCommand.contains(callbackCommand) }
    }
  }
}