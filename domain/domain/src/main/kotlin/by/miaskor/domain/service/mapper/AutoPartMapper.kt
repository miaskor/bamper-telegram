package by.miaskor.domain.service.mapper

import by.miaskor.cloud.drive.domain.FilePath
import by.miaskor.cloud.drive.service.CloudDriveService
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.model.AutoPartVO
import by.miaskor.domain.model.AutoPartWithPhotoUrlVO
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class AutoPartMapper(private val cloudDriveService: CloudDriveService) {

  fun mapWithPhotoUrl(autoPartWithPhotoUrlVO: AutoPartWithPhotoUrlVO): Mono<AutoPartResponse> {
    return Mono.fromSupplier { FilePath(autoPartWithPhotoUrlVO.photoPath) }
      .flatMap(cloudDriveService::getDownloadUrl)
      .map { photoUrl ->
        AutoPartResponse(
          description = autoPartWithPhotoUrlVO.description,
          photoDownloadUrl = photoUrl,
          price = autoPartWithPhotoUrlVO.price,
          quality = autoPartWithPhotoUrlVO.quality,
          currency = autoPartWithPhotoUrlVO.currency,
          partNumber = autoPartWithPhotoUrlVO.partNumber,
          model = autoPartWithPhotoUrlVO.model,
          brand = autoPartWithPhotoUrlVO.brandName,
          year = autoPartWithPhotoUrlVO.year,
          body = autoPartWithPhotoUrlVO.body,
          transmission = autoPartWithPhotoUrlVO.transmission,
          engineCapacity = autoPartWithPhotoUrlVO.engineCapacity,
          fuelType = autoPartWithPhotoUrlVO.fuelType,
          engineType = autoPartWithPhotoUrlVO.engineType,
          autoPartName = autoPartWithPhotoUrlVO.autoPartName,
        )
      }.subscribeOn(Schedulers.boundedElastic())
  }

  fun mapWithPhoto(list: List<AutoPartVO>): Mono<ResponseWithLimit<AutoPartWithPhotoResponse>> {
    return Mono.fromSupplier { if (list.size > 10) list.dropLast(1) else list }
      .flatMapIterable { it }
      .flatMap(::mapWithPhoto)
      .collectList()
      .map { autoParts ->
        val isMoreExists = list.size > 10
        ResponseWithLimit(
          entities = autoParts,
          isMoreExists = isMoreExists
        )
      }
  }

  fun mapWithPhoto(autoPartVO: AutoPartVO): Mono<AutoPartWithPhotoResponse> {
    return Mono.fromCallable { FilePath(autoPartVO.photoPath) }
      .flatMap(cloudDriveService::downloadFile)
      .map { byteArrayPhoto ->
        AutoPartWithPhotoResponse(
          id = autoPartVO.id.toString(),
          description = autoPartVO.description,
          photo = byteArrayPhoto,
          price = autoPartVO.price,
          quality = autoPartVO.quality,
          currency = autoPartVO.currency,
          partNumber = autoPartVO.partNumber,
          model = autoPartVO.model,
          brand = autoPartVO.brandName,
          year = autoPartVO.year,
          autoPartEN = autoPartVO.autoPartEN,
          autoPartRU = autoPartVO.autoPartRU,
        )
      }.subscribeOn(Schedulers.boundedElastic())
  }
}
