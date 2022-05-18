package by.miaskor.bot.service

import com.pengrad.telegrambot.model.Update

val Update.chatId: Long
  get() {
    return this.message().chat().id()
  }

val Update.text: String
  get() {
    return this.message().text() ?: ""
  }

val Update.username: String
  get() {
    return this.message().chat().username()
  }
