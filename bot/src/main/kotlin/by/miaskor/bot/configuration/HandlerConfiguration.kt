package by.miaskor.bot.configuration

import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Configuration

@Configuration
open class HandlerConfiguration(
  private val telegramBot: TelegramBot,
  private val serviceConfiguration: ServiceConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

}
