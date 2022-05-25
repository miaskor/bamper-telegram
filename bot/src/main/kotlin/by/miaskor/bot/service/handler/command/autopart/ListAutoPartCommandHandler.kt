package by.miaskor.bot.service.handler.command.autopart

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendPhoto
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.domain.AutoPartResponse
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ListAutoPartCommandHandler(
  private val telegramBot: TelegramBot,
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = Command.LIST_AUTO_PART

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, messageSettings: MessageSettings): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .map { it.currentStoreHouseId() }
      .flatMap(autoPartConnector::getAllByStoreHouseId)
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, autoParts: List<AutoPartResponse>): Mono<Unit> {
    return Flux.fromIterable(autoParts)
      .map {
        telegramBot.sendPhoto(update.chatId, it.photo, it.toString())
      }.then(Mono.empty())
  }
}
