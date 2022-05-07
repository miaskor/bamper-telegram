package by.miaskor.bot.domain

enum class Command(private vararg val commands: String) {
  CHANGE_LANGUAGE("Изменить язык", "Change language");

  fun isCommand(command: String): Boolean {
    return this.commands.contains(command)
  }
}
