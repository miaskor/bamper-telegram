package by.miaskor.domain.configuration

import by.miaskor.cloud.drive.service.ImageUploader
import by.miaskor.domain.service.AutoPartService
import by.miaskor.domain.service.BrandService
import by.miaskor.domain.service.CarService
import by.miaskor.domain.service.StoreHouseService
import by.miaskor.domain.service.TelegramClientService
import by.miaskor.domain.service.WorkerTelegramService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val repositoryConfiguration: RepositoryConfiguration,
  private val uploader: ImageUploader
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

  @Bean
  open fun storeHouseService(): StoreHouseService {
    return StoreHouseService(repositoryConfiguration.storeHouseRepository())
  }

  @Bean
  open fun brandService(): BrandService {
    return BrandService(repositoryConfiguration.brandRepository())
  }

  @Bean
  open fun carService(): CarService {
    return CarService(repositoryConfiguration.carRepository(), storeHouseService())
  }

  @Bean
  open fun autoPartService(): AutoPartService {
    return AutoPartService(
      repositoryConfiguration.autoPartRepository(),
      uploader,
      storeHouseService()
    )
  }
}
