package by.miaskor.bot.configuration

import by.miaskor.bot.service.handler.ChooseLanguageHandler
import by.miaskor.bot.service.handler.MainMenuHandler
import by.miaskor.bot.service.handler.StateHandlerRegistry
import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class HandlerConfiguration(
  private val telegramBot: TelegramBot,
  private val serviceConfiguration: ServiceConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

}
