package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.extension.sendPhoto
import by.miaskor.bot.service.extension.sendPhotoWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import kotlin.reflect.KFunction1

object MessageSender {

  lateinit var keyboardBuilder: KeyboardBuilder
  lateinit var telegramBot: TelegramBot

  fun <T> sendPhoto(
    chatId: Long,
    photo: ByteArray,
    messageFunction: KFunction1<MessageSettings, String>,
    vararg formatValues: String
  ): Mono<T> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        val message = messageFunction.invoke(it)
        telegramBot.sendPhoto(chatId, photo, message.format(*formatValues))
      }.then(Mono.empty())
  }

  fun <T> sendPhotoWithInlineKeyboard(
    chatId: Long,
    photo: ByteArray,
    messageFunction: KFunction1<MessageSettings, String>,
    keyboardFunction: KFunction1<KeyboardSettings, List<String>>,
    callbackCommand: List<CallbackCommand>,
    vararg formatValues: String
  ): Mono<T> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .flatMap { messageSettings ->
        Mono.just(chatId)
          .resolveLanguage(KeyboardSettings::class)
          .map { keyboardBuilder.buildInlineKeyboard(keyboardFunction.invoke(it), callbackCommand) }
          .map { keyboard ->
            val message = messageFunction.invoke(messageSettings)
            telegramBot.sendPhotoWithKeyboard(chatId, photo, message.format(*formatValues), keyboard)
          }
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

  fun <T> sendMessageWithKeyboard(
    chatId: Long,
    messageFunction: KFunction1<MessageSettings, String>,
    vararg formatValues: String
  ): Mono<T> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(chatId))
      .map { (messageSettings, keyboard) ->
        val message = messageFunction.invoke(messageSettings)
        telegramBot.sendMessageWithKeyboard(chatId, message.format(*formatValues), keyboard)
      }.then(Mono.empty())
  }

  fun <T> sendMessageWithInlineKeyboard(
    chatId: Long,
    messageFunction: KFunction1<MessageSettings, String>,
    keyboardFunction: KFunction1<KeyboardSettings, List<String>>,
    callbackCommand: List<CallbackCommand>,
    vararg formatValues: String
  ): Mono<T> {
    return Mono.just(chatId)
      .resolveLanguage(MessageSettings::class)
      .flatMap { messageSettings ->
        Mono.just(chatId)
          .resolveLanguage(KeyboardSettings::class)
          .map { keyboardBuilder.buildInlineKeyboard(keyboardFunction.invoke(it), callbackCommand) }
          .map { keyboard ->
            val message = messageFunction.invoke(messageSettings)
            telegramBot.sendMessageWithKeyboard(chatId, message.format(*formatValues), keyboard)
          }
      }.then(Mono.empty())
  }
}
