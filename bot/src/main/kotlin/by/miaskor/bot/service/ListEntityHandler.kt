package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.KeyboardSettings
import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.CallbackQuery
import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_NEXT
import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_PREV
import by.miaskor.bot.domain.CallbackQuery.CARS_NEXT
import by.miaskor.bot.domain.CallbackQuery.CARS_PREV
import by.miaskor.bot.domain.CallbackQuery.Companion.listAutoParts
import by.miaskor.bot.domain.CallbackQuery.Companion.listCars
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.MessageSender.sendMessageWithInlineKeyboard
import by.miaskor.bot.service.MessageSender.sendPhoto
import by.miaskor.bot.service.MessageSender.sendPhotoWithInlineKeyboard
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.connector.CarConnector
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KFunction1

class ListEntityHandler(
  private val listEntityCache: ListEntityCache,
  private val listSettings: ListSettings,
  private val carConnector: CarConnector,
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache
) {

  fun handle(update: Update, callbackQuery: CallbackQuery): Mono<Unit> {
    return Mono.just(update.chatId)
      .filter { !listEntityCache.isExists(it) }
      .doOnNext { listEntityCache.populate(update.chatId) }
      .then(
        Mono.just(update.chatId)
          .flatMap(telegramClientCache::getTelegramClient)
          .flatMap { telegramClient ->
            when (callbackQuery) {
              CARS_NEXT, CARS_PREV -> sendCars(update, callbackQuery, telegramClient)
              AUTO_PARTS_NEXT, AUTO_PARTS_PREV -> sendAutoParts(update, callbackQuery, telegramClient)
            }
          }
      )
  }

  private fun sendCars(update: Update, callbackQuery: CallbackQuery, telegramClient: TelegramClient): Mono<Unit> {
    return Mono.just(update.chatId)
      .doOnNext {
        if (callbackQuery == CARS_NEXT) {
          listEntityCache.nextCars(it)
        } else {
          listEntityCache.prevCars(it)
        }
      }.map { chatId ->
        StoreHouseIdRequest(
          storeHouseId = telegramClient.currentStoreHouseId(),
          limit = listSettings.limit(),
          offset = listEntityCache.getCarOffset(chatId)
        )
      }
      .flatMap(carConnector::getAllByStoreHouseId)
      .flatMap { responseWithLimit ->
        sendEntities(
          update,
          responseWithLimit,
          MessageSettings::listCarMessage,
          CarResponse::disassembly
        )
      }
  }

  private fun sendAutoParts(update: Update, callbackQuery: CallbackQuery, telegramClient: TelegramClient): Mono<Unit> {
    return Mono.just(update.chatId)
      .doOnNext {
        if (callbackQuery == AUTO_PARTS_NEXT) {
          listEntityCache.nextAutoParts(it)
        } else {
          listEntityCache.prevAutoParts(it)
        }
      }.map { chatId ->
        StoreHouseIdRequest(
          storeHouseId = telegramClient.currentStoreHouseId(),
          limit = listSettings.limit(),
          offset = listEntityCache.getAutoPartOffset(chatId)
        )
      }
      .flatMap(autoPartConnector::getAllByStoreHouseId)
      .flatMap { responseWithLimit ->
        sendEntities(
          update,
          responseWithLimit,
          MessageSettings::listAutoPartMessage
        ) { AutoPartResponse.disassembly(it, telegramClient.chatLanguage) }
      }
  }

  private fun <T> sendEntities(
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
      is CarResponse -> sendMessage(chatId, messageFunction, *componentsDisassembler.invoke(entity))
      is AutoPartResponse -> sendPhoto(
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
      is CarResponse -> sendMessageWithInlineKeyboard(
        chatId, messageFunction, KeyboardSettings::keyboardForLists, listCars, *componentsDisassembler.invoke(entity)
      )

      is AutoPartResponse -> sendPhotoWithInlineKeyboard(
        chatId,
        entity.photo,
        messageFunction,
        KeyboardSettings::keyboardForLists,
        listAutoParts,
        *componentsDisassembler.invoke(entity)
      )

      else -> Mono.empty()
    }
  }
}
