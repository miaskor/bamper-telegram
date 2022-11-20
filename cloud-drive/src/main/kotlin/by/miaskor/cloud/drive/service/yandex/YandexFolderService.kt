package by.miaskor.cloud.drive.service.yandex

import by.miaskor.cloud.drive.connector.yandex.YandexCloudDriveConnector
import by.miaskor.cloud.drive.service.FolderService
import reactor.core.publisher.Mono

class YandexFolderService(private val yandexCloudDriveConnector: YandexCloudDriveConnector) : FolderService {
  override fun createIfRequire(fullPath: String): Mono<Unit> {
    return yandexCloudDriveConnector.isFolderExists(fullPath)
      .flatMap { isExists ->
        if (isExists)
          Mono.empty()
        else
          yandexCloudDriveConnector.createFolder(fullPath)
      }
  }
}
