package by.miaskor.bot.configuration

import by.miaskor.bot.service.CommandResolver
import by.miaskor.bot.service.handler.BotStateHandlerRegistry
import by.miaskor.bot.service.handler.ChangeLanguageCommandHandler
import by.miaskor.bot.service.handler.ChooseLanguageHandler
import by.miaskor.bot.service.handler.CommandHandlerRegistry
import by.miaskor.bot.service.handler.GreetingsHandler
import by.miaskor.bot.service.handler.MainMenuHandler
import by.miaskor.bot.telegram.Bot
import com.pengrad.telegrambot.TelegramBot
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BotConfiguration(
  private val settingsConfiguration: SettingsConfiguration,
  private val serviceConfiguration: ServiceConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

  @Bean
  open fun telegramBot(): TelegramBot {
    return TelegramBot
      .Builder(settingsConfiguration.botSettings().token())
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
      settingsConfiguration.stateSettings(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun mainMenuHandler(): MainMenuHandler {
    return MainMenuHandler(commandResolver())
  }

  @Bean
  open fun greetingsHandler(): GreetingsHandler {
    return GreetingsHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      settingsConfiguration.stateSettings()
    )
  }

  @Bean
  open fun commandHandlerRegistry(): CommandHandlerRegistry {
    return CommandHandlerRegistry(
      changeLanguageCommandHandler()
    )
  }

  @Bean
  open fun changeLanguageCommandHandler(): ChangeLanguageCommandHandler {
    return ChangeLanguageCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      serviceConfiguration.commandSettingsRegistry(),
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun commandResolver(): CommandResolver {
    return CommandResolver(commandHandlerRegistry())
  }
}
