package by.miaskor.domain.service

import by.miaskor.cloud.drive.domain.UploadFileRequest
import by.miaskor.cloud.drive.service.ImageUploader
import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.CarAutoPartDto
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdRequest
import by.miaskor.domain.api.domain.StoreHouseRequestWithConstraint
import by.miaskor.domain.repository.AutoPartRepository
import by.miaskor.domain.service.telegram.TelegramApiService
import by.miaskor.domain.tables.pojos.AutoPart
import reactor.core.publisher.Mono

class AutoPartService(
  private val autoPartRepository: AutoPartRepository,
  private val uploader: ImageUploader,
  private val autoPartMapper: AutoPartMapper,
  private val telegramApiService: TelegramApiService,
) {

  fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Boolean> {
    return autoPartRepository.deleteByStoreHouseIdAndId(storeHouseId, id)
      .flatMap(::isDeletionSuccessful)
      .onErrorReturn(false)
  }

  fun create(autoPartDto: AutoPartDto): Mono<Unit> {
    val autoPartKey = AutoPartKeyGenerator.generate()
    return telegramApiService.getPhotoPath(autoPartDto.photoId)
      .map { photoPath ->
        UploadFileRequest(
          autoPartDto.chatId,
          telegramApiService.getPhoto(photoPath),
          autoPartKey
        )
      }.flatMap(uploader::upload)
      .map {
        AutoPart(
          description = autoPartDto.description,
          price = autoPartDto.price,
          quality = if (autoPartDto.quality) 1 else 0,
          currency = autoPartDto.currency,
          partNumber = autoPartDto.partNumber,
          carId = autoPartDto.carId,
          carPartId = autoPartDto.carPartId,
          storeHouseId = autoPartDto.storeHouseId,
          photoPath = it.path,
          autoPartKey = autoPartKey
        )
      }.flatMap(autoPartRepository::save)
  }

  fun getAllByStoreHouseId(storeHouseIdRequest: StoreHouseIdRequest): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return autoPartRepository.findAllByStoreHouseId(
      storeHouseIdRequest.storeHouseId,
      storeHouseIdRequest.limit + 1,
      storeHouseIdRequest.offset
    ).flatMap(autoPartMapper::mapWithPhoto)
  }

  fun getAllByStoreHouseIdAndCarAndCarPart(carAutoPartDto: CarAutoPartDto):
      Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return autoPartRepository.findAllByStoreHouseIdAndCarAndCarPart(carAutoPartDto)
      .flatMap(autoPartMapper::mapWithPhoto)
  }

  fun getAllByStoreHouseIdAndPartNumber(
    storeHouseIdRequest: StoreHouseRequestWithConstraint,
  ): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return autoPartRepository.findAllByStoreHouseIdAndPartNumber(
      storeHouseIdRequest.constraint,
      storeHouseIdRequest.storeHouseId,
      storeHouseIdRequest.limit + 1,
      storeHouseIdRequest.offset
    ).flatMap(autoPartMapper::mapWithPhoto)
  }

  fun getByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<AutoPartWithPhotoResponse> {
    return autoPartRepository.findByStoreHouseIdAndId(storeHouseId, id)
      .flatMap(autoPartMapper::downloadPhoto)
  }

  fun getByTelegramChatIdAndId(telegramChatId: Long, id: Long): Mono<AutoPartResponse> {
    return autoPartRepository.findByTelegramChatIdAndId(telegramChatId, id)
      .flatMap(autoPartMapper::mapWithPhotoUrl)
  }

  private fun isDeletionSuccessful(deletedRecords: Int): Mono<Boolean> {
    return if (deletedRecords > 0)
      Mono.just(true)
    else
      Mono.empty()
  }
}
