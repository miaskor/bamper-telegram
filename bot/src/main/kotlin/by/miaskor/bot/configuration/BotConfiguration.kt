package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.BotSettings
import by.miaskor.bot.service.handler.BotStateHandlerRegistry
import by.miaskor.bot.service.handler.ChooseLanguageHandler
import by.miaskor.bot.service.handler.GreetingsHandler
import by.miaskor.bot.service.handler.MainMenuHandler
import by.miaskor.bot.telegram.Bot
import com.pengrad.telegrambot.TelegramBot
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BotConfiguration(
  private val botSettings: BotSettings,
  private val serviceConfiguration: ServiceConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

  @Bean
  open fun telegramBot(): TelegramBot {
    return TelegramBot
      .Builder(botSettings.token())
      .okHttpClient(OkHttpClient())
      .build()
  }

  @Bean
  open fun bot(): Bot {
    return Bot(
      telegramBot(),
      stateHandlerRegistry(),
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun stateHandlerRegistry(): BotStateHandlerRegistry {
    return BotStateHandlerRegistry(
      chooseLanguageHandler(),
      mainMenuHandler(),
      greetingsHandler()
    )
  }

  @Bean
  open fun chooseLanguageHandler(): ChooseLanguageHandler {
    return ChooseLanguageHandler(
      telegramBot(),
      connectorConfiguration.telegramClientConnector(),
      serviceConfiguration.telegramClientCache(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun mainMenuHandler(): MainMenuHandler {
    return MainMenuHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun greetingsHandler(): GreetingsHandler {
    return GreetingsHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }
}
