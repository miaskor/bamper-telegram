package by.miaskor.domain.service

import by.miaskor.cloud.drive.domain.UploadFileRequest
import by.miaskor.cloud.drive.service.ImageUploader
import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.repository.AutoPartRepository
import by.miaskor.domain.service.telegram.TelegramApiService
import by.miaskor.domain.tables.pojos.AutoPart
import reactor.core.publisher.Mono

class AutoPartService(
  private val autoPartRepository: AutoPartRepository,
  private val uploader: ImageUploader,
  private val storeHouseService: StoreHouseService,
  private val telegramApiService: TelegramApiService
) {

  fun create(autoPartDto: AutoPartDto): Mono<Unit> {
    return Mono.fromSupplier {
      AutoPart(
        description = autoPartDto.description,
        price = autoPartDto.price,
        quality = if (autoPartDto.quality) 1 else 0,
        currency = autoPartDto.currency,
        partNumber = autoPartDto.partNumber,
        carId = autoPartDto.carId,
        carPartId = autoPartDto.carPartId,
        storeHouseId = autoPartDto.storeHouseId
      )
    }.flatMap(autoPartRepository::create)
      .flatMap { setPhotoPath(it, autoPartDto) }
  }

  private fun setPhotoPath(autoPartId: Long, autoPartDto: AutoPartDto): Mono<Unit> {
    return telegramApiService.getPhotoPath(autoPartDto.photoId)
      .map { photoPath ->
        UploadFileRequest(
          autoPartDto.chatId,
          telegramApiService.getPhoto(photoPath),
          false,
          autoPartId
        )
      }.flatMap(uploader::upload)
      .flatMap { autoPartRepository.updatePhotoPathById(it.path, autoPartId) }
  }
}
