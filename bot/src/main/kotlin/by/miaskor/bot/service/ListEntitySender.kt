package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.CallbackQuery
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KFunction1

object ListEntitySender {

  fun <T> sendEntities(
    update: Update,
    responseWithLimit: ResponseWithLimit<T>,
    messageFunction: KFunction1<MessageSettings, String>,
    componentsDisassembler: (T) -> Array<String>
  ): Mono<Unit> {
    return Flux.fromIterable(responseWithLimit.entities)
      .skipLast(1)
      .flatMap { sendMessage<T>(update.chatId, it, messageFunction, componentsDisassembler) }
      .then(
        Mono.justOrEmpty(responseWithLimit.entities.lastOrNull())
          .flatMap { sendLast(it, update.chatId, componentsDisassembler, messageFunction) }
      )
  }

  private fun <T> sendMessage(
    chatId: Long,
    entity: T,
    messageFunction: KFunction1<MessageSettings, String>,
    componentsDisassembler: (T) -> Array<String>
  ): Mono<T> {
    return when (entity) {
      is CarResponse -> MessageSender.sendMessage(chatId, messageFunction, *componentsDisassembler.invoke(entity))
      is AutoPartResponse -> MessageSender.sendPhoto(
        chatId,
        entity.photo,
        messageFunction,
        *componentsDisassembler.invoke(entity)
      )

      else -> Mono.empty()
    }
  }

  private fun <T> sendLast(
    entity: T,
    chatId: Long,
    componentsDisassembler: (T) -> Array<String>,
    messageFunction: KFunction1<MessageSettings, String>
  ): Mono<Unit> {
    return when (entity) {
      is CarResponse -> MessageSender.sendMessageWithInlineKeyboard(
        chatId,
        messageFunction,
        KeyboardSettings::keyboardForLists,
        CallbackQuery.listCars,
        *componentsDisassembler.invoke(entity)
      )

      is AutoPartResponse -> MessageSender.sendPhotoWithInlineKeyboard(
        chatId,
        entity.photo,
        messageFunction,
        KeyboardSettings::keyboardForLists,
        CallbackQuery.listAutoParts,
        *componentsDisassembler.invoke(entity)
      )

      else -> Mono.empty()
    }
  }
}