package by.miaskor.bot.configuration

import by.miaskor.bot.service.CommandResolver
import by.miaskor.bot.service.handler.command.BackCommandHandler
import by.miaskor.bot.service.handler.command.ChangeLanguageCommandHandler
import by.miaskor.bot.service.handler.command.CommandHandlerRegistry
import by.miaskor.bot.service.handler.command.EmployeesCommandHandler
import by.miaskor.bot.service.handler.command.UndefinedCommandHandler
import by.miaskor.bot.service.handler.state.BotStateHandlerRegistry
import by.miaskor.bot.service.handler.state.ChooseLanguageHandler
import by.miaskor.bot.service.handler.state.EmployeesMenuHandler
import by.miaskor.bot.service.handler.state.GreetingsHandler
import by.miaskor.bot.service.handler.state.MainMenuHandler
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
      greetingsHandler(),
      employeesMenuHandler()
    )
  }

  @Bean
  open fun chooseLanguageHandler(): ChooseLanguageHandler {
    return ChooseLanguageHandler(
      telegramBot(),
      connectorConfiguration.telegramClientConnector(),
      serviceConfiguration.telegramClientCache(),
      settingsConfiguration.stateSettingsEN(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun mainMenuHandler(): MainMenuHandler {
    return MainMenuHandler(commandResolver())
  }

  @Bean
  open fun employeesMenuHandler(): EmployeesMenuHandler {
    return EmployeesMenuHandler(
      commandResolver()
    )
  }

  @Bean
  open fun greetingsHandler(): GreetingsHandler {
    return GreetingsHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder(),
      settingsConfiguration.stateSettingsEN()
    )
  }

  @Bean
  open fun commandHandlerRegistry(): CommandHandlerRegistry {
    return CommandHandlerRegistry(
      changeLanguageCommandHandler(),
      employeesCommandHandler(),
      backCommandHandler(),
      undefinedCommandHandler()
    )
  }

  @Bean
  open fun changeLanguageCommandHandler(): ChangeLanguageCommandHandler {
    return ChangeLanguageCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun undefinedCommandHandler(): UndefinedCommandHandler {
    return UndefinedCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun employeesCommandHandler(): EmployeesCommandHandler {
    return EmployeesCommandHandler(
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun backCommandHandler(): BackCommandHandler {
    return BackCommandHandler(
      serviceConfiguration.telegramClientCache(),
      telegramBot(),
      serviceConfiguration.keyboardBuilder()
    )
  }

  @Bean
  open fun commandResolver(): CommandResolver {
    return CommandResolver(commandHandlerRegistry())
  }
}
