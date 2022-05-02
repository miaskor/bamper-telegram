package by.miaskor.bot.configuration

import by.miaskor.bot.service.TelegramClientCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration() {

  @Bean
  open fun telegramClientCache(): TelegramClientCache {
    return TelegramClientCache()
  }
}
