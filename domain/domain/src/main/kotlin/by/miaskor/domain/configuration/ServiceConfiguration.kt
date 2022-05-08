package by.miaskor.domain.configuration

import by.miaskor.domain.service.TelegramClientService
import by.miaskor.domain.service.WorkerTelegramService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val repositoryConfiguration: RepositoryConfiguration
) {

  @Bean
  open fun telegramClientService(): TelegramClientService {
    return TelegramClientService(
      workerTelegramService(),
      repositoryConfiguration.telegramClientRepository()
    )
  }

  @Bean
  open fun workerTelegramService(): WorkerTelegramService {
    return WorkerTelegramService(repositoryConfiguration.workerTelegramRepository())
  }
}
