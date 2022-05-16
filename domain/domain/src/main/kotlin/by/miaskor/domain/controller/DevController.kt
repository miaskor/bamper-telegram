package by.miaskor.domain.controller

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.domain.UploadFileRequest
import by.miaskor.cloud.drive.service.ImageDownloader
import by.miaskor.cloud.drive.service.ImageUploader
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/dev")
class DevController(
  private val uploader: ImageUploader,
  private val imageDownloader: ImageDownloader
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
        entityId = entityId.toLong(),
        isAdvertisement = isAdvertisement.toBoolean()
      )
    }
      .flatMap(uploader::upload)
      .map { ResponseEntity.ok(it.path) }
  }

  @PostMapping(path = ["/download"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
  fun upload(@RequestBody downloadFile: DownloadFile, response: HttpServletResponse) {
    val image = imageDownloader.download(downloadFile)

    DataBufferUtils.write(image, response.outputStream)
      .map(DataBufferUtils::release)
      .blockLast()
  }
}
