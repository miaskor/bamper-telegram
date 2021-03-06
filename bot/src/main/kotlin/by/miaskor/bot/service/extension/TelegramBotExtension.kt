package by.miaskor.bot.service.extension

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto

fun TelegramBot.sendMessage(chatId: Long, message: String) {
  this.execute(SendMessage(chatId, message))
}

fun TelegramBot.sendPhoto(chatId: Long, photo: ByteArray, message: String) {
  this.execute(SendPhoto(chatId, photo).caption(message))
}

fun TelegramBot.sendPhotoWithKeyboard(chatId: Long, photo: ByteArray, message: String, keyboard: Keyboard) {
  this.execute(SendPhoto(chatId, photo).caption(message).replyMarkup(keyboard))
}

fun TelegramBot.sendMessageWithKeyboard(chatId: Long, message: String, keyboard: Keyboard) {
  this.execute(SendMessage(chatId, message).replyMarkup(keyboard))
}
