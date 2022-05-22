package by.miaskor.bot.configuration

import by.miaskor.bot.service.handler.state.ProcessingStepService
import by.miaskor.bot.telegram.Bot
import com.pengrad.telegrambot.TelegramBot
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BotConfiguration(
  private val settingsConfiguration: SettingsConfiguration,
  private val serviceConfiguration: ServiceConfiguration
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
      serviceConfiguration.telegramClientCache()
    )
  }

  @Bean
  open fun processingStepService(): ProcessingStepService {
    return ProcessingStepService(
      serviceConfiguration.creationCarStepValidation(),
      serviceConfiguration.keyboardBuilder(),
      telegramBot()
    )
  }
}
