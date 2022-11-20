package by.miaskor.cloud.drive.service

import reactor.core.publisher.Mono

interface FolderService {

  fun createIfRequire(fullPath: String): Mono<Unit>
}
