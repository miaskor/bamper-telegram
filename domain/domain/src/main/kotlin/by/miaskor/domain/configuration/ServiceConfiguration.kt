package by.miaskor.domain.configuration

import by.miaskor.domain.service.TelegramClientService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val repositoryConfiguration: RepositoryConfiguration
) {

  @Bean
  open fun telegramClientService(): TelegramClientService {
    return TelegramClientService(repositoryConfiguration.telegramClientRepository())
  }
}
