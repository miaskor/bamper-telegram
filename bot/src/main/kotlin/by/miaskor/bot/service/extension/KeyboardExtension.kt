package by.miaskor.bot.service.extension

import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup

@Suppress("UNCHECKED_CAST")
fun ReplyKeyboardMarkup.addRowsToBegin(keyboardValues: Array<Array<String>>): ReplyKeyboardMarkup {
  val keyboardButtons: MutableList<Array<String>> = mutableListOf()
  keyboardValues.forEach {
    keyboardButtons.add(it)
  }
  javaClass.getDeclaredField("keyboard").let {
    it.isAccessible = true
    val keyboards: List<List<KeyboardButton>> = it.get(this) as List<List<KeyboardButton>>
    keyboards.flatten()
      .forEach { keyboardButton ->
        val text = keyboardButton.javaClass.getDeclaredField("text").let { text ->
          text.isAccessible = true
          val textValue = text.get(keyboardButton) as String
          textValue
        }
        keyboardButtons.add(arrayOf(text))
      }
  }
  return ReplyKeyboardMarkup(keyboardButtons.toTypedArray(), true, false, true)
}
