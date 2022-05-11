package by.miaskor.bot.service.handler.command.employee

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.ADDING_EMPLOYEE
import by.miaskor.bot.domain.Command.EMPLOYEE
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.bot.service.chatId
import by.miaskor.bot.service.extension.sendMessage
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import by.miaskor.bot.service.handler.command.CommandHandler
import by.miaskor.bot.service.text
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.connector.WorkerTelegramConnector
import by.miaskor.domain.api.domain.TelegramClientResponse
import by.miaskor.domain.api.domain.WorkerTelegramDto
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono

class EmployeeCommandHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector,
  private val workerTelegramConnector: WorkerTelegramConnector,
  private val keyboardBuilder: KeyboardBuilder,
  private val telegramClientCache: TelegramClientCache
) : CommandHandler {
  override val command = EMPLOYEE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.fromSupplier { update.text.replace("@", "") }
      .flatMap { telegramClientConnector.getByUsername(it) }
      .switchIfEmpty(sendFailMessage(update))
      .flatMap { handle(update, it) }
  }

  private fun handle(update: Update, telegramClientResponse: TelegramClientResponse): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .flatMap {
        if (telegramClientResponse.chatId == update.chatId) {
          sendIsYouMessage(update)
        } else if (it.currentBotState == ADDING_EMPLOYEE) {
          createEmployee(telegramClientResponse, update)
        } else {
          deleteEmployee(telegramClientResponse, update)
        }
      }
  }

  private fun createEmployee(telegramClientResponse: TelegramClientResponse, update: Update): Mono<Unit> {
    return Mono.just(telegramClientResponse.chatId)
      .map {
        WorkerTelegramDto(
          employerChatId = update.chatId,
          employeeChatId = it
        )
      }.flatMap { createEmployee(it, update) }
  }

  private fun deleteEmployee(telegramClientResponse: TelegramClientResponse, update: Update): Mono<Unit> {
    return Mono.just(telegramClientResponse.chatId)
      .map {
        WorkerTelegramDto(
          employerChatId = update.chatId,
          employeeChatId = it
        )
      }.flatMap(workerTelegramConnector::remove)
      .then(
        telegramClientCache.getTelegramClient(update.chatId)
          .map { it.modified() }
      )
      .then(sendSuccessRemovingMessage(update))
  }

  private fun createEmployee(workerTelegramDto: WorkerTelegramDto, update: Update): Mono<Unit> {
    return Mono.fromSupplier {
      WorkerTelegramDto(
        employerChatId = workerTelegramDto.employeeChatId,
        employeeChatId = workerTelegramDto.employerChatId
      )
    }.flatMap(workerTelegramConnector::find)
      .switchIfEmpty(
        workerTelegramConnector.create(workerTelegramDto)
          .then(
            telegramClientCache.getTelegramClient(update.chatId)
              .map { it.modified() }
          )
          .then(sendSuccessAddingMessage(update))
      )
      .flatMap { sendIsYourEmployerMessage(update) }
  }

  private fun sendSuccessAddingMessage(update: Update): Mono<WorkerTelegramDto> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map {
        telegramBot.sendMessageWithKeyboard(update.chatId, it.t1.addingEmployeeSuccessMessage(), it.t2)
      }.then(Mono.empty())
  }

  private fun sendSuccessRemovingMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map {
        telegramBot.sendMessageWithKeyboard(update.chatId, it.t1.removingEmployeeSuccessMessage(), it.t2)
      }.then(Mono.empty())
  }

  private fun sendFailMessage(update: Update): Mono<TelegramClientResponse> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(update.chatId, it.failFoundEmployeeMessage().format(update.text))
      }.then(Mono.empty())
  }

  private fun sendIsYourEmployerMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(update.chatId, it.employeeIsYourEmployerMessage().format(update.text))
      }.then(Mono.empty())
  }

  private fun sendIsYouMessage(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .resolveLanguage(MessageSettings::class)
      .map {
        telegramBot.sendMessage(update.chatId, it.employeeIsYouMessage().format(update.text))
      }.then(Mono.empty())
  }
}
