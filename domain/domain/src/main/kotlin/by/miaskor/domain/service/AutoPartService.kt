package by.miaskor.domain.service

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.domain.UploadFileRequest
import by.miaskor.cloud.drive.service.ImageDownloader
import by.miaskor.cloud.drive.service.ImageUploader
import by.miaskor.domain.api.domain.AutoPartDto
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.domain.model.AutoPartVO
import by.miaskor.domain.repository.AutoPartRepository
import by.miaskor.domain.service.telegram.TelegramApiService
import by.miaskor.domain.tables.pojos.AutoPart
import org.springframework.core.io.buffer.DataBufferUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class AutoPartService(
  private val autoPartRepository: AutoPartRepository,
  private val uploader: ImageUploader,
  private val downloader: ImageDownloader,
  private val telegramApiService: TelegramApiService
) {

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

  fun getAllByStoreHouseId(storeHouseId: Long): Mono<List<AutoPartResponse>> {
    return autoPartRepository.findAllByStoreHouseId(storeHouseId)
      .flatMapIterable { it }
      .flatMap { downloadPhoto(it) }
      .collectList()
  }

  private fun downloadPhoto(autoPartVO: AutoPartVO): Mono<AutoPartResponse> {
    return Mono.just(autoPartVO.photoPath)
      .flatMap { path ->
        DataBufferUtils.join(downloader.download(DownloadFile(path)))
      }.map { photo ->
        val byteArrayPhoto = ByteArray(photo.capacity())
        photo.read(byteArrayPhoto)
        AutoPartResponse(
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
