package by.miaskor.domain.service

import by.miaskor.cloud.drive.domain.upload.UploadFile
import by.miaskor.cloud.drive.service.CloudDriveService
import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.CarAutoPartDto
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.api.domain.StoreHouseIdWithConstraint
import by.miaskor.domain.api.domain.StoreHouseIdWithLimitRequest
import by.miaskor.domain.repository.AutoPartRepository
import by.miaskor.domain.service.mapper.AutoPartMapper
import by.miaskor.domain.service.telegram.TelegramService
import by.miaskor.domain.tables.pojos.AutoPart
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class AutoPartService(
  private val autoPartRepository: AutoPartRepository,
  private val cloudDriveService: CloudDriveService,
  private val autoPartMapper: AutoPartMapper,
  private val telegramService: TelegramService,
) {

  fun deleteByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<Boolean> {
    return autoPartRepository.deleteByStoreHouseIdAndId(storeHouseId, id)
      .flatMap(::isDeletionSuccessful)
      .onErrorReturn(false)
  }

  fun create(autoPartDto: AutoPartDto): Mono<Unit> {
    val autoPartKey = AutoPartKeyGenerator.generate()
    val imagePath = ImagePathGenerator.generate(autoPartDto.chatId.toString(), autoPartKey)
    return Mono.fromSupplier {
      UploadFile(
        imagePath,
        telegramService.getPhoto(autoPartDto.photoId),
      )
    }
      .flatMap(cloudDriveService::uploadFile)
      .map { uploadFileResponse ->
        AutoPart(
          description = autoPartDto.description,
          price = autoPartDto.price,
          quality = if (autoPartDto.quality) 1 else 0,
          currency = autoPartDto.currency,
          partNumber = autoPartDto.partNumber,
          carId = autoPartDto.carId,
          carPartId = autoPartDto.carPartId,
          storeHouseId = autoPartDto.storeHouseId,
          photoPath = uploadFileResponse.value,
          autoPartKey = autoPartKey
        )
      }
      .flatMap(autoPartRepository::save)
      .subscribeOn(Schedulers.boundedElastic())
  }

  fun getAllByStoreHouseId(storeHouseIdWithLimitRequest: StoreHouseIdWithLimitRequest): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return autoPartRepository.findAllByStoreHouseId(
      storeHouseIdWithLimitRequest.storeHouseId,
      storeHouseIdWithLimitRequest.limit + 1,
      storeHouseIdWithLimitRequest.offset
    )
      .flatMap(autoPartMapper::mapWithPhoto)
  }

  fun getAllByStoreHouseIdAndCarAndCarPart(carAutoPartDto: CarAutoPartDto):
      Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return autoPartRepository.findAllByStoreHouseIdAndCarAndCarPart(carAutoPartDto)
      .flatMap(autoPartMapper::mapWithPhoto)
  }

  fun getAllByStoreHouseIdAndPartNumber(
    storeHouseIdRequest: StoreHouseIdWithConstraint,
  ): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return autoPartRepository.findAllByStoreHouseIdAndPartNumber(
      storeHouseIdRequest.constraint,
      storeHouseIdRequest.storeHouseId,
      storeHouseIdRequest.limit + 1,
      storeHouseIdRequest.offset
    )
      .flatMap(autoPartMapper::mapWithPhoto)
  }

  fun getByStoreHouseIdAndId(storeHouseId: Long, id: Long): Mono<AutoPartWithPhotoResponse> {
    return autoPartRepository.findByStoreHouseIdAndId(storeHouseId, id)
      .flatMap(autoPartMapper::mapWithPhoto)
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
