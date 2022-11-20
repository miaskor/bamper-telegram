package by.miaskor.cloud.drive.configuration

import by.miaskor.cloud.drive.service.CloudDriveService
import by.miaskor.cloud.drive.service.FileDownloader
import by.miaskor.cloud.drive.service.FileUploader
import by.miaskor.cloud.drive.service.FolderService
import by.miaskor.cloud.drive.service.yandex.YandexCloudDriveService
import by.miaskor.cloud.drive.service.yandex.YandexFileDownloader
import by.miaskor.cloud.drive.service.yandex.YandexFileUploader
import by.miaskor.cloud.drive.service.yandex.YandexFolderService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ServiceConfiguration(private val connectorConfiguration: ConnectorConfiguration) {

  @Bean
  open fun folderService(): FolderService {
    return YandexFolderService(connectorConfiguration.yandexCloudDriveConnector())
  }

  @Bean
  open fun fileDownloader(): FileDownloader {
    return YandexFileDownloader(connectorConfiguration.yandexCloudDriveConnector())
  }

  @Bean
  open fun cloudDriveService(): CloudDriveService {
    return YandexCloudDriveService(
      yandexCloudDriveConnector = connectorConfiguration.yandexCloudDriveConnector(),
      fileDownloader = fileDownloader(),
      fileUploader = fileUploader()
    )
  }

  @Bean
  open fun fileUploader(): FileUploader {
    return YandexFileUploader(
      yandexCloudDriveConnector = connectorConfiguration.yandexCloudDriveConnector(),
      folderService = folderService()
    )
  }
}
