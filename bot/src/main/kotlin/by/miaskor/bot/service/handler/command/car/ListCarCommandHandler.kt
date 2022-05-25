package by.miaskor.bot.service.handler.command.car

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command.LIST_CAR
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithInlineKeyboard
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.CarConnector
import by.miaskor.domain.api.domain.CarResponse
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ListCarCommandHandler(
  private val telegramBot: TelegramBot,
  private val carConnector: CarConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = LIST_CAR

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, messageSettings: MessageSettings): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .map { it.currentStoreHouseId() }
      .flatMap(carConnector::getAllByStoreHouseId)
      .flatMap { handle(update, messageSettings, it) }
  }

  private fun handle(update: Update, messageSettings: MessageSettings, cars: List<CarResponse>): Mono<Unit> {
    return Flux.fromIterable(cars)
      .skipLast(1)
      .map {
        telegramBot.sendMessage(update.chatId, buildCarMessage(messageSettings.listCarMessage(), it))
      }.then(
        Mono.justOrEmpty(cars.lastOrNull())
          .map {
            telegramBot.sendMessageWithInlineKeyboard(
              update.chatId,
              buildCarMessage(
                messageSettings.listCarMessage(),
                it
              )
            )
          }
      ).then(Mono.empty())
  }

  private fun buildCarMessage(message: String, carResponse: CarResponse): String {
    return carResponse.let {
      message.format(
        it.id,
        it.brandName,
        it.model,
        it.year,
        it.body,
        it.transmission,
        it.engineCapacity,
        it.fuelType,
        it.engineType
      )
    }
  }
}
