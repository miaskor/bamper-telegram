package by.miaskor.bot.service.handler.command

import by.miaskor.bot.configuration.settings.MessageSettings
import by.miaskor.bot.domain.BotState.CHOOSING_STORE_HOUSE
import by.miaskor.bot.domain.BotState.EMPLOYEES_MENU
import by.miaskor.bot.domain.BotState.FINDING_AUTO_PART
import by.miaskor.bot.domain.BotState.MAIN_MENU
import by.miaskor.bot.domain.BotState.MODIFICATION_STORE_HOUSE_MENU
import by.miaskor.bot.domain.BotState.READ_ONLY_STORE_HOUSE_MENU
import by.miaskor.bot.domain.Command.BACK
import by.miaskor.bot.domain.TelegramClient
import by.miaskor.bot.service.BotStateChanger.changeBotState
import by.miaskor.bot.service.KeyboardBuilder
import by.miaskor.bot.service.LanguageSettingsResolver.resolveLanguage
import by.miaskor.bot.service.cache.TelegramClientCache
import by.miaskor.bot.service.extension.chatId
import by.miaskor.bot.service.extension.pollLast
import by.miaskor.bot.service.extension.sendMessageWithKeyboard
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class BackCommandHandler(
  private val telegramClientCache: TelegramClientCache,
  private val telegramBot: TelegramBot,
  private val keyboardBuilder: KeyboardBuilder,
) : CommandHandler {
  override val command = BACK

  override fun handle(update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .flatMap(telegramClientCache::getTelegramClient)
      .flatMap { handle(it, update) }
  }

  private fun handle(telegramClient: TelegramClient, update: Update): Mono<Unit> {
    return Mono.just(update.chatId)
      .changeBotState(update::chatId, telegramClient.previousBotStates.pollLast(), true)
      .resolveLanguage(MessageSettings::class)
      .flatMap { handle(it, update) }
  }

  private fun handle(messageSettings: MessageSettings, update: Update): Mono<Unit> {
    return telegramClientCache.getTelegramClient(update.chatId)
      .zipWith(keyboardBuilder.build(update.chatId))
      .map { (telegramClient, keyboard) ->
        val sendMessage = when (telegramClient.currentBotState) {
          MAIN_MENU -> messageSettings.mainMenuMessage()
          EMPLOYEES_MENU -> messageSettings.employeesMenuMessage()
          CHOOSING_STORE_HOUSE -> messageSettings.allStoreHousesMessage()
          MODIFICATION_STORE_HOUSE_MENU, READ_ONLY_STORE_HOUSE_MENU -> messageSettings.storeHouseMenuMessage()
            .format(telegramClient.currentStoreHouseName())

          FINDING_AUTO_PART -> messageSettings.findAutoPartMenuMessage()
          else -> "Something bad happened"
        }
        telegramBot.sendMessageWithKeyboard(update.chatId, sendMessage, keyboard)
      }.then(Mono.empty())
  }
}
