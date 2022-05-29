package by.miaskor.domain.configuration

import by.miaskor.cloud.drive.service.ImageDownloader
import by.miaskor.cloud.drive.service.ImageUploader
import by.miaskor.domain.service.AutoPartService
import by.miaskor.domain.service.BrandService
import by.miaskor.domain.service.CarPartService
import by.miaskor.domain.service.CarService
import by.miaskor.domain.service.StoreHouseService
import by.miaskor.domain.service.TelegramClientService
import by.miaskor.domain.service.WorkerStoreHouseService
import by.miaskor.domain.service.WorkerTelegramService
import by.miaskor.domain.service.telegram.TelegramApiService
import org.cfg4j.provider.ConfigurationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val repositoryConfiguration: RepositoryConfiguration,
  private val uploader: ImageUploader,
  private val imageDownloader: ImageDownloader,
  private val confProvider: ConfigurationProvider,
  private val connectorConfiguration: ConnectorConfiguration
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
    return CarService(repositoryConfiguration.carRepository())
  }

  @Bean
  open fun carPartService(): CarPartService {
    return CarPartService(repositoryConfiguration.carPartRepository())
  }

  @Bean
  open fun telegramApiService(): TelegramApiService {
    val getPhotoPathUrl = confProvider.getProperty("bot.getPhotoPathUrl", String::class.java)
    val getPhotoUrl = confProvider.getProperty("bot.getPhotoUrl", String::class.java)
    return TelegramApiService(
      connectorConfiguration.telegramWebClient(),
      getPhotoPathUrl,
      getPhotoUrl
    )
  }

  @Bean
  open fun autoPartService(): AutoPartService {
    return AutoPartService(
      repositoryConfiguration.autoPartRepository(),
      uploader,
      imageDownloader,
      telegramApiService()
    )
  }
  @Bean
  open fun workerStoreHouseService(): WorkerStoreHouseService {
    return WorkerStoreHouseService(
      repositoryConfiguration.workerStoreHouseRepository()
    )
  }
}
