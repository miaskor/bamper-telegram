package by.miaskor.bot.service.extension

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.request.SendMessage

fun TelegramBot.sendMessage(chatId: Long, message: String) {
  this.execute(SendMessage(chatId, message))
}

fun TelegramBot.sendMessageWithKeyboard(chatId: Long, message: String, keyboard: Keyboard) {
  this.execute(SendMessage(chatId, message).replyMarkup(keyboard))
}
