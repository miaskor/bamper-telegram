package by.miaskor.domain.configuration

import by.miaskor.domain.repository.AdvertisementRepository
import by.miaskor.domain.repository.BrandRepository
import by.miaskor.domain.repository.CarRepository
import by.miaskor.domain.repository.JooqAdvertisementRepository
import by.miaskor.domain.repository.JooqBrandRepository
import by.miaskor.domain.repository.JooqCarRepository
import by.miaskor.domain.repository.JooqStoreHouseRepository
import by.miaskor.domain.repository.JooqTelegramClientRepository
import by.miaskor.domain.repository.JooqWorkerTelegramRepository
import by.miaskor.domain.repository.StoreHouseRepository
import by.miaskor.domain.repository.TelegramClientRepository
import by.miaskor.domain.repository.WorkerTelegramRepository
import org.jooq.DSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RepositoryConfiguration(
  private val dslContext: DSLContext
) {

  @Bean
  open fun brandRepository(): BrandRepository {
    return JooqBrandRepository(dslContext)
  }

  @Bean
  open fun advertisementRepository(): AdvertisementRepository {
    return JooqAdvertisementRepository(dslContext)
  }

  @Bean
  open fun carRepository(): CarRepository {
    return JooqCarRepository(dslContext)
  }

  @Bean
  open fun telegramClientRepository(): TelegramClientRepository {
    return JooqTelegramClientRepository(dslContext)
  }

  @Bean
  open fun workerTelegramRepository(): WorkerTelegramRepository {
    return JooqWorkerTelegramRepository(dslContext)
  }

  @Bean
  open fun storeHouseRepository(): StoreHouseRepository {
    return JooqStoreHouseRepository(dslContext)
  }
}
