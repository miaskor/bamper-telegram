package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import kotlin.reflect.KFunction1

object MessageSender {

  lateinit var keyboardBuilder: KeyboardBuilder
  lateinit var telegramBot: TelegramBot

  fun <T> sendMessage(chatId: Long, messageFunction: KFunction1<MessageSettings, String>): Mono<T> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(chatId, messageFunction.invoke(it))
      }.then(Mono.empty())
  }

  fun <T> sendMessage(
    chatId: Long,
    messageFunction: KFunction1<MessageSettings, String>,
    vararg formatValues: String
  ): Mono<T> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        val message = messageFunction.invoke(it)
        telegramBot.sendMessage(chatId, message.format(*formatValues))
      }.then(Mono.empty())
  }

  fun <T> sendMessageWithKeyboard(chatId: Long, messageFunction: KFunction1<MessageSettings, String>): Mono<T> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(chatId))
      .map { (messageSettings, keyboard) ->
        telegramBot.sendMessageWithKeyboard(chatId, messageFunction.invoke(messageSettings), keyboard)
      }.then(Mono.empty())
  }
}
