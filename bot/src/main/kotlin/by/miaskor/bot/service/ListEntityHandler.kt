package by.miaskor.bot.service

import by.miaskor.bot.configuration.settings.ListSettings
import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.CallbackQuery
import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_NEXT
import by.miaskor.bot.domain.CallbackQuery.AUTO_PARTS_PREV
import by.miaskor.bot.domain.CallbackQuery.CARS_NEXT
import by.miaskor.bot.domain.CallbackQuery.CARS_PREV
import by.miaskor.bot.domain.ListEntityType.Companion.getByCallbackQuery
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.ListEntitySender.sendEntities
import by.miaskor.bot.service.cache.AbstractListCache
import by.miaskor.bot.service.cache.ListEntityCacheRegistry
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.connector.CarConnector
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.CarResponse
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class ListEntityHandler(
  private val listEntityCacheRegistry: ListEntityCacheRegistry,
  private val listSettings: ListSettings,
  private val carConnector: CarConnector,
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache
) {

  fun handle(update: Update, callbackQuery: CallbackQuery): Mono<Unit> {
    return Mono.fromSupplier { listEntityCacheRegistry.lookup(getByCallbackQuery(callbackQuery)) }
      .doOnNext { it.populateIsNotExists(update.chatId) }
      .doOnNext { it.movePointer(update.chatId, callbackQuery) }
      .zipWith(telegramClientCache.getTelegramClient(update.chatId))
      .flatMap { (listEntityCache, telegramClient) ->
        handle(update, callbackQuery, listEntityCache, telegramClient)
      }
  }

  private fun handle(
    update: Update,
    callbackQuery: CallbackQuery,
    listEntityCache: AbstractListCache,
    telegramClient: TelegramClient
  ): Mono<Unit> {
    return Mono.fromSupplier {
      StoreHouseIdRequest(
        storeHouseId = telegramClient.currentStoreHouseId(),
        limit = listSettings.limit(),
        offset = listEntityCache.getOffset(update.chatId)
      )
    }
      .flatMap { storeHouseIdRequest ->
        when (callbackQuery) {
          CARS_NEXT, CARS_PREV -> sendCars(update, storeHouseIdRequest)
          AUTO_PARTS_NEXT, AUTO_PARTS_PREV -> sendAutoParts(update, storeHouseIdRequest, telegramClient)
        }
      }
  }

  private fun sendCars(
    update: Update,
    storeHouseIdRequest: StoreHouseIdRequest
  ): Mono<Unit> {
    return Mono.just(storeHouseIdRequest)
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

  private fun sendAutoParts(
    update: Update,
    storeHouseIdRequest: StoreHouseIdRequest,
    telegramClient: TelegramClient
  ): Mono<Unit> {
    return Mono.just(storeHouseIdRequest)
      .flatMap(autoPartConnector::getAllByStoreHouseId)
      .flatMap { responseWithLimit ->
        sendEntities(
          update,
          responseWithLimit,
          MessageSettings::listAutoPartMessage
        ) { AutoPartResponse.disassembly(it, telegramClient.chatLanguage) }
      }
  }
}
