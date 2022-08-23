package by.miaskor.domain.configuration

import by.miaskor.cloud.drive.configuration.ServiceConfiguration
import by.miaskor.domain.service.AutoPartService
import by.miaskor.domain.service.BamperClientService
import by.miaskor.domain.service.BrandService
import by.miaskor.domain.service.CarPartService
import by.miaskor.domain.service.CarService
import by.miaskor.domain.service.StoreHouseConstraintHandler
import by.miaskor.domain.service.StoreHouseService
import by.miaskor.domain.service.TelegramClientService
import by.miaskor.domain.service.WorkerStoreHouseService
import by.miaskor.domain.service.WorkerTelegramService
import by.miaskor.domain.service.telegram.TelegramConnector
import by.miaskor.domain.service.telegram.TelegramService
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val repositoryConfiguration: RepositoryConfiguration,
  private val serviceConfiguration: ServiceConfiguration,
  private val confProvider: ConfigurationProvider,
  private val mapperConfiguration: MapperConfiguration,
  private val connectorConfiguration: ConnectorConfiguration,
) {

  @Bean
  open fun telegramClientService(): TelegramClientService {
    return TelegramClientService(
      repositoryConfiguration.telegramClientRepository(),
      mapperConfiguration.telegramClientMapper()
    )
  }

  @Bean
  open fun workerTelegramService(): WorkerTelegramService {
    return WorkerTelegramService(
      repositoryConfiguration.workerTelegramRepository(),
      workerStoreHouseService()
    )
  }

  @Bean
  open fun storeHouseService(): StoreHouseService {
    return StoreHouseService(
      repositoryConfiguration.storeHouseRepository(),
      workerStoreHouseService(),
      mapperConfiguration.storeHouseMapper()
    )
  }

  @Bean
  open fun brandService(): BrandService {
    return BrandService(
      repositoryConfiguration.brandRepository(),
      mapperConfiguration.brandMapper()
    )
  }

  @Bean
  open fun carService(): CarService {
    return CarService(
      repositoryConfiguration.carRepository(),
      mapperConfiguration.carMapper()
    )
  }

  @Bean
  open fun carPartService(): CarPartService {
    return CarPartService(repositoryConfiguration.carPartRepository())
  }

  @Bean
  open fun telegramConnector(): TelegramConnector {
    return TelegramConnector(connectorConfiguration.telegramWebClient())
  }

  @Bean
  open fun telegramService(): TelegramService {
    return TelegramService(
      telegramConnector()
    )
  }

  @Bean
  open fun autoPartService(): AutoPartService {
    return AutoPartService(
      repositoryConfiguration.autoPartRepository(),
      serviceConfiguration.imageUploader(),
      mapperConfiguration.autoPartMapper(),
      telegramService()
    )
  }

  @Bean
  open fun workerStoreHouseService(): WorkerStoreHouseService {
    return WorkerStoreHouseService(
      repositoryConfiguration.workerStoreHouseRepository()
    )
  }

  @Bean
  open fun storeHouseConstraintHandler(): StoreHouseConstraintHandler {
    return StoreHouseConstraintHandler(autoPartService())
  }

  @Bean
  open fun bamperClientService(): BamperClientService {
    return BamperClientService(repositoryConfiguration.bamperClientRepository())
  }
}
