package by.miaskor.bot.service.handler.list

import by.miaskor.bot.configuration.settings.ExecutorSettings
import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.CallbackCommand
import by.miaskor.bot.service.MessageSender
import by.miaskor.bot.service.extension.chatId
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import kotlin.reflect.KFunction1

object ListEntitySender {

  lateinit var executorSettings: ExecutorSettings

  fun <T> sendEntities(
    update: Update,
    responseWithLimit: ResponseWithLimit<T>,
    messageFunction: KFunction1<MessageSettings, String>,
    entityDisassembler: (T) -> Array<String>,
  ): Mono<Unit> {
    return Flux.fromIterable(responseWithLimit.entities)
      .skipLast(1)
      .parallel(executorSettings.maxConcurrency())
      .runOn(Schedulers.newParallel("list-entity-sender-thread", executorSettings.maxConcurrency()))
      .concatMap { sendEntity(update.chatId, it, messageFunction, entityDisassembler) }
      .sequential()
      .then(
        Mono.justOrEmpty(responseWithLimit.entities.lastOrNull())
          .flatMap { sendLast(it, update.chatId, entityDisassembler, messageFunction) }
      )
  }

  fun <T> sendEntity(
    chatId: Long,
    entity: T,
    messageFunction: KFunction1<MessageSettings, String>,
    entityDisassembler: (T) -> Array<String>,
  ): Mono<T> {
    return when (entity) {
      is CarResponse -> MessageSender.sendMessage(chatId, messageFunction, *entityDisassembler.invoke(entity))
      is AutoPartWithPhotoResponse -> MessageSender.sendPhoto(
        chatId,
        entity.photo,
        messageFunction,
        *entityDisassembler.invoke(entity)
      )

      else -> Mono.empty()
    }
  }

  private fun <T> sendLast(
    entity: T,
    chatId: Long,
    entityDisassembler: (T) -> Array<String>,
    messageFunction: KFunction1<MessageSettings, String>,
  ): Mono<Unit> {
    return when (entity) {
      is CarResponse -> MessageSender.sendMessageWithInlineKeyboard(
        chatId,
        messageFunction,
        KeyboardSettings::keyboardForLists,
        CallbackCommand.listCars,
        *entityDisassembler.invoke(entity)
      )

      is AutoPartWithPhotoResponse -> MessageSender.sendPhotoWithInlineKeyboard(
        chatId,
        entity.photo,
        messageFunction,
        KeyboardSettings::keyboardForLists,
        CallbackCommand.listAutoParts,
        *entityDisassembler.invoke(entity)
      )

      else -> Mono.empty()
    }
  }
}
