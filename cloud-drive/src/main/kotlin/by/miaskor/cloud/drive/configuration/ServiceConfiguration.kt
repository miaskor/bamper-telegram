package by.miaskor.cloud.drive.configuration

import by.miaskor.cloud.drive.service.DefaultFilePathGenerator
import by.miaskor.cloud.drive.service.DefaultFolderCreator
import by.miaskor.cloud.drive.service.DefaultImageDownloader
import by.miaskor.cloud.drive.service.DefaultImageUploader
import by.miaskor.cloud.drive.service.FilePathGenerator
import by.miaskor.cloud.drive.service.FolderCreator
import by.miaskor.cloud.drive.service.ImageDownloader
import by.miaskor.cloud.drive.service.ImageUploader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(
  private val connectorConfiguration: ConnectorConfiguration
) {

  @Bean
  open fun filePathGenerator(): FilePathGenerator {
    return DefaultFilePathGenerator()
  }

  @Bean
  open fun folderCreator(): FolderCreator {
    return DefaultFolderCreator(connectorConfiguration.cloudYandexDriveConnector())
  }

  @Bean
  open fun imageDownloader(): ImageDownloader {
    return DefaultImageDownloader(connectorConfiguration.cloudYandexDriveConnector())
  }

  @Bean
  open fun imageUploader(): ImageUploader {
    return DefaultImageUploader(
      connectorConfiguration.cloudYandexDriveConnector(),
      folderCreator(),
      filePathGenerator()
    )
  }
}
