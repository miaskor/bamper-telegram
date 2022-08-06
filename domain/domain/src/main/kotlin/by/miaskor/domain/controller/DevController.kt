package by.miaskor.domain.controller

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.domain.UploadFileRequest
import by.miaskor.cloud.drive.service.CloudYandexDriveService
import by.miaskor.cloud.drive.service.ImageDownloader
import by.miaskor.cloud.drive.service.ImageUploader
import by.miaskor.domain.service.AutoPartKeyGenerator
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/dev")
class DevController(
  private val uploader: ImageUploader,
  private val imageDownloader: ImageDownloader,
  private val cloudYandexDriveService: CloudYandexDriveService,
) {

  @PostMapping(path = ["/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun upload(
    @RequestPart("image") image: Mono<FilePart>,
    @RequestPart("chat_id") chatId: String,
    @RequestPart("entity_id") entityId: String,
    @RequestPart("is_advertisement") isAdvertisement: String,
  ): Mono<ResponseEntity<String>> {
    return Mono.fromSupplier {
      UploadFileRequest(
        chatId = chatId.toLong(),
        image = image.block()!!.content(),
        uniqueKey = AutoPartKeyGenerator.generate()
      )
    }
      .flatMap(uploader::upload)
      .map { ResponseEntity.ok(it.path) }
  }

  @PostMapping(path = ["/download"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
  fun download(@RequestBody downloadFile: DownloadFile, response: HttpServletResponse) {
    val image = imageDownloader.download(downloadFile)

    DataBufferUtils.write(image, response.outputStream)
      .map(DataBufferUtils::release)
      .blockLast()
  }

  @GetMapping(path = ["/download-url"])
  fun downloadUrl(@RequestParam("photoPath") photoPath: String): Mono<String> {
    return Mono.just(photoPath)
      .map { DownloadFile(path = it) }
      .flatMap { cloudYandexDriveService.getDownloadUrl(it) }
  }
}
