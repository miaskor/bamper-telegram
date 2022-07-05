package by.miaskor.bot.service.handler.command.bamper

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Command.AUTH_BAMPER
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.MessageSender.sendMessage
import by.miaskor.bot.service.MessageSender.sendMessageWithKeyboard
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.text
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.connector.BamperIntegrationConnector
import by.miaskor.domain.AuthDto
import by.miaskor.domain.api.connector.BamperClientConnector
import by.miaskor.domain.api.domain.BamperClientDto
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.DeleteMessage
import reactor.core.publisher.Mono

class AuthBamperCommandHandler(
  private val bamperIntegrationConnector: BamperIntegrationConnector,
  private val bamperClientConnector: BamperClientConnector,
  private val telegramBot: TelegramBot,
  private val telegramClientCache: TelegramClientCache,
) : CommandHandler {
  override val command = AUTH_BAMPER

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { telegramClient -> handle(update, telegramClient) }
  }

  private fun handle(update: Update, telegramClient: TelegramClient): Mono<Unit> {
    return Mono.just(update)
      .filter { telegramClient.isAuth() }
      .switchIfEmpty(
        processCredentials(update, telegramClient)
          .then(Mono.empty())
      )
      .flatMap { auth(update) }
  }

  private fun processCredentials(update: Update, telegramClient: TelegramClient): Mono<Unit> {
    return Mono.just(update.text)
      .map(::disassembleCredentials)
      .flatMap { authDto -> processCredentials(update, authDto, telegramClient) }
  }

  private fun processCredentials(update: Update, authDto: AuthDto, telegramClient: TelegramClient): Mono<Unit> {
    return bamperIntegrationConnector.auth(authDto)
      .map { bamperSessionId -> telegramClientCache.updateBamperSessionId(update.chatId, bamperSessionId) }
      .switchIfEmpty(sendMessage(update.chatId, MessageSettings::authIsFailedMessage))
      .flatMap { successAuth(update, authDto) }
  }

  private fun successAuth(update: Update, authDto: AuthDto): Mono<Unit> {
    return Mono.just(update)
      .flatMap {
        telegramBot.execute(DeleteMessage(update.chatId, update.message().messageId()))
        bamperClientConnector.create(BamperClientDto(authDto.login, authDto.password))
      }.then(auth(update))
  }

  private fun auth(update: Update): Mono<Unit> {
    return Mono.just(update)
      .changeBotState(update::chatId, BotState.BAMPER_MENU)
      .then(sendMessageWithKeyboard(update.chatId, MessageSettings::bamperMenuMessage))
  }

  private companion object {
    private fun disassembleCredentials(credentials: String): AuthDto {
      val (login, password) = credentials.split(", ")
      return AuthDto(login, password)
    }
  }
}
