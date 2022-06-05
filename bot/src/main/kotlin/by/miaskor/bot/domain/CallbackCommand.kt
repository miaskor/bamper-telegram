package by.miaskor.bot.domain

enum class CallbackCommand {
  CARS_NEXT,
  CARS_PREV,
  AUTO_PARTS_NEXT,
  AUTO_PARTS_PREV;

  companion object {
    val listCars = listOf(CARS_NEXT, CARS_PREV)
    val listAutoParts = listOf(AUTO_PARTS_NEXT, AUTO_PARTS_PREV)
  }
}
