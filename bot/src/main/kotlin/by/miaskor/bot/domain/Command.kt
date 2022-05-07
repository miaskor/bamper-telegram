package by.miaskor.bot.domain

enum class Command(vararg val commands: String) {
  CHANGE_LANGUAGE("Изменить язык", "Change language"),
  EMPLOYEES("Работники", "Employees");

  fun isCommand(command: String): Boolean {
    return this.commands.contains(command)
  }
}
