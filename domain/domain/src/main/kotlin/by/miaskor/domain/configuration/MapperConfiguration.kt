package by.miaskor.domain.configuration

import by.miaskor.cloud.drive.configuration.ServiceConfiguration
import by.miaskor.domain.service.mapper.AutoPartMapper
import by.miaskor.domain.service.mapper.BrandMapper
import by.miaskor.domain.service.mapper.CarMapper
import by.miaskor.domain.service.mapper.StoreHouseMapper
import by.miaskor.domain.service.mapper.TelegramClientMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MapperConfiguration(
  private val serviceConfiguration: ServiceConfiguration,
) {

  @Bean
  open fun autoPartMapper(): AutoPartMapper {
    return AutoPartMapper(serviceConfiguration.cloudYandexDriveService(), serviceConfiguration.imageDownloader())
  }

  @Bean
  open fun brandMapper(): BrandMapper {
    return BrandMapper()
  }

  @Bean
  open fun carMapper(): CarMapper {
    return CarMapper()
  }

  @Bean
  open fun storeHouseMapper(): StoreHouseMapper {
    return StoreHouseMapper()
  }

  @Bean
  open fun telegramClientMapper(): TelegramClientMapper {
    return TelegramClientMapper()
  }
}
