package by.miaskor.cloud.drive.service

import by.miaskor.cloud.drive.service.connector.CloudYandexDriveConnector
import reactor.core.publisher.Mono

interface FolderCreator {

  fun create(folderName: String): Mono<Unit>
}

class DefaultFolderCreator(
  private val cloudYandexDriveConnector: CloudYandexDriveConnector
) : FolderCreator {
  override fun create(folderName: String): Mono<Unit> {
    return cloudYandexDriveConnector.createFolder(folderName)
  }
}
