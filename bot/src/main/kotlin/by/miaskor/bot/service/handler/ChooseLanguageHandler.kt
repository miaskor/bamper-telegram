package by.miaskor.bot.service.handler

import by.miaskor.bot.domain.BotState
import by.miaskor.bot.domain.Language.Companion.getDomainByFullLanguage
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.TelegramClientCache
import by.miaskor.domain.api.connector.TelegramClientConnector
import by.miaskor.domain.api.domain.TelegramClientRequest
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import reactor.core.publisher.Mono

class ChooseLanguageHandler(
  private val telegramBot: TelegramBot,
  private val telegramClientConnector: TelegramClientConnector,
  private val telegramClientCache: TelegramClientCache,
  private val keyboardBuilder: KeyboardBuilder
) : BotStateHandler {
  override val state: BotState = BotState.CHOOSE_LANGUAGE

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.message())
      .filter(::isMessageAppropriate)
      .switchIfEmpty(
        Mono.fromSupplier {
          telegramBot.execute(
            SendMessage(
              update.message().chat().id(),
              "Выберите язык русский или английский. Choose the language english or russian"
            )
          )
        }.then(Mono.empty())
      )
      .flatMap(::handle)
  }

  private fun handle(message: Message): Mono<Unit> {
    return Mono.just(message)
      .flatMap(::createTelegramClient)
      .then(Mono.defer { sendMessage(message) })
  }

  private fun sendMessage(message: Message): Mono<Unit> {
    return Mono.just(message)
      .flatMap { keyboardBuilder.build(message.chat().id()) }
      .map {
        telegramBot.execute(
          SendMessage(message.chat().id(), "It is main menu of bot")
            .replyMarkup(it)
        )
      }
      .changeBotState { message.chat().id() }
  }

  private fun createTelegramClient(message: Message): Mono<Unit> {
    return Mono.just(message)
      .map {
        TelegramClientRequest(
          chatId = it.chat().id().toString(),
          chatLanguage = getDomainByFullLanguage(it.text()).domain,
          username = it.chat().username(),
        )
      }
      .flatMap(telegramClientConnector::create)
      .then(
        telegramClientCache.getTelegramClient(message.chat().id())
          .map {
            telegramClientCache.populate(
              message.chat().id(),
              it.copy(chatLanguage = getDomainByFullLanguage(message.text()).domain)
            )
          }
      )
  }

  private fun isMessageAppropriate(message: Message): Boolean {
    return message.text() == "English" || message.text() == "Русский"
  }
}
