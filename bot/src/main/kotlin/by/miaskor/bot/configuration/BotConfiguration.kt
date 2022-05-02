package by.miaskor.bot.configuration

import by.miaskor.bot.configuration.settings.BotSettings
import by.miaskor.bot.service.handler.ChooseLanguageHandler
import by.miaskor.bot.service.handler.MainMenuHandler
import by.miaskor.bot.service.handler.StateHandlerRegistry
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
  open fun stateHandlerRegistry(): StateHandlerRegistry {
    return StateHandlerRegistry(
      chooseLanguageHandler(),
      mainMenuHandler()
    )
  }


  @Bean
  open fun chooseLanguageHandler(): ChooseLanguageHandler {
    return ChooseLanguageHandler(
      telegramBot(),
      serviceConfiguration.telegramClientCache(),
      connectorConfiguration.telegramClientConnector()
    )
  }

  @Bean
  open fun mainMenuHandler(): MainMenuHandler {
    return MainMenuHandler(
      telegramBot()
    )
  }
}
