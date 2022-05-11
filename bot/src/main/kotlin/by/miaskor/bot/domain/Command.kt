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

  BACK("Назад", "Back"),
  UNDEFINED(".*");

  fun isCommand(command: String): Boolean {
    return this.commands.firstOrNull { command.matches(Regex(it)) } != null
  }
}
