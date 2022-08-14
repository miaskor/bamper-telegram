package by.miaskor.domain.service

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.service.CloudYandexDriveService
import by.miaskor.cloud.drive.service.ImageDownloader
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.api.domain.AutoPartWithPhotoResponse
import by.miaskor.domain.api.domain.ResponseWithLimit
import by.miaskor.domain.model.AutoPartVO
import by.miaskor.domain.model.AutoPartWithPhotoUrlVO
import org.springframework.core.io.buffer.DataBufferUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

class AutoPartMapper(
  private val cloudYandexDriveService: CloudYandexDriveService,
  private val downloader: ImageDownloader,
) {

  fun mapWithPhotoUrl(autoPartWithPhotoUrlVO: AutoPartWithPhotoUrlVO): Mono<AutoPartResponse> {
    return Mono.fromCallable { DownloadFile(autoPartWithPhotoUrlVO.photoPath) }
      .flatMap(cloudYandexDriveService::getDownloadUrl)
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
    return Flux.fromIterable(list)
      .flatMap(::downloadPhoto)
      .collectList()
      .map {
        val isMoreExists = it.size > 10
        val autoParts = if (isMoreExists) it.dropLast(1) else it
        ResponseWithLimit(
          entities = autoParts,
          isMoreExists = isMoreExists
        )
      }
  }

  fun downloadPhoto(autoPartVO: AutoPartVO): Mono<AutoPartWithPhotoResponse> {
    return Mono.just(autoPartVO.photoPath)
      .flatMap { path ->
        DataBufferUtils.join(downloader.download(DownloadFile(path)))
      }.map { photo ->
        val byteArrayPhoto = ByteArray(photo.capacity())
        photo.read(byteArrayPhoto)
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
      }
  }
}
