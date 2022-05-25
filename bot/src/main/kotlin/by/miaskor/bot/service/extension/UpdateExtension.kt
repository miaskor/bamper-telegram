package by.miaskor.bot.service

import com.pengrad.telegrambot.model.Update

val Update.chatId: Long
  get() {
    return this.message()?.chat()?.id() ?: this.callbackQuery().from().id()
  }

val Update.text: String
  get() {
    return this.message()?.text() ?: this.callbackQuery().data()
  }

val Update.username: String
  get() {
    return this.message()?.chat()?.username() ?: this.callbackQuery().from().username()
  }

val Update.photoId: String
  get() {
    return this.message()?.photo()?.last()?.fileId() ?: ""
  }

fun Update.info(): String {
  return """
    Username -> ${this.username}
    Message -> ${this.text}  
    """
}
