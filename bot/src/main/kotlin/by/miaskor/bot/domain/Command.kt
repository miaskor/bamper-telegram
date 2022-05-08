package by.miaskor.bot.domain

enum class Command(private vararg val commands: String) {
  CHANGE_LANGUAGE("Изменить язык", "Change language"),
  BACK("Назад", "Back"),
  EMPLOYEE("@.{1,32}"),
  LANGUAGE("Русский", "English"),
  EMPLOYEES("Работники", "Employees"),
  ADD_EMPLOYEE("Добавить работника", "Add employee"),
  LIST_EMPLOYEE("Список работников", "List of employees"),
  UNDEFINED;

  fun isCommand(command: String): Boolean {
    return this.commands.firstOrNull { command.matches(Regex(it)) } != null
  }
}
