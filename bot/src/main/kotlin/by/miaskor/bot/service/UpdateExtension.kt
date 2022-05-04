package by.miaskor.bot.service

import com.pengrad.telegrambot.model.Update

fun Update.chatId(): Long {
  return this.message().chat().id()
}

fun Update.text(): String {
  return this.message().text()
}

fun Update.username(): String {
  return this.message().chat().username()
}
