package by.miaskor.bot.service.handler.command.autopart

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.Command
import by.miaskor.bot.domain.Language
import by.miaskor.bot.domain.Language.ENGLISH
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendPhoto
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.domain.api.connector.AutoPartConnector
import by.miaskor.domain.api.domain.AutoPartResponse
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class ListAutoPartCommandHandler(
  private val telegramBot: TelegramBot,
  private val autoPartConnector: AutoPartConnector,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = Command.LIST_AUTO_PART

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(telegramClientCache.getTelegramClient(update.chatId))
      .flatMap { (messageSettings, telegramClient) ->
        handle(update, messageSettings, telegramClient)
      }
  }

  private fun handle(update: Update, messageSettings: MessageSettings, telegramClient: TelegramClient): Mono<Unit> {
    return Mono.just(telegramClient.currentStoreHouseId())
      .flatMap(autoPartConnector::getAllByStoreHouseId)
      .flatMapIterable { it }
      .map {
        telegramBot.sendPhoto(
          update.chatId,
          it.photo,
          buildAutoPartMessage(
            messageSettings.listAutoPartMessage(),
            it,
            Language.getByDomain(telegramClient.chatLanguage)
          )
        )
      }.then(Mono.empty())
  }

  private fun buildAutoPartMessage(message: String, autoPartResponse: AutoPartResponse, language: Language): String {
    return autoPartResponse.let {
      message.format(
        if (language == ENGLISH) it.autoPartEN else it.autoPartRU,
        it.brand,
        it.model,
        it.year,
        it.description,
        it.partNumber,
        it.price,
        it.currency,
        it.quality
      )
    }
  }
}
