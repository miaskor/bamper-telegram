package by.miaskor.bot.domain

enum class Command(private vararg val commands: String) {
  CHANGE_LANGUAGE("Изменить язык", "Change language"),
  LANGUAGE("Russian", "English", "Английский"),

  EMPLOYEE("@.{5,32}"),
  EMPLOYEES("Работники", "Employees"),
  ADD_EMPLOYEE("Добавить работника", "Add employee"),
  REMOVE_EMPLOYEE("Удалить работника", "Remove employee"),
  LIST_EMPLOYEE("Список работников", "List of employees"),

  STORE_HOUSE(".{1,10}"),
  CREATE_STORE_HOUSE("Create store house", "Создать склад"),
  CHOOSE_STORE_HOUSE("Choose store house", "Выбрать склад"),
  SELECT_CERTAIN_STORE_HOUSE(".{1,10}"),

  CREATE_SPARE_PART("Add spare part", "Добавить запчасть"),

  CREATE_CAR("Add car", "Добавить машину"),

  BACK("Back", "Назад"),
  NEXT_STEP("Next step", "Следующий шаг"),
  PREVIOUS_STEP("Previous step", "Предыдущий шаг"),
  UNDEFINED(".*");

  fun isCommand(command: String): Boolean {
    return this.commands.firstOrNull { command.matches(Regex(it)) } != null
  }
}
