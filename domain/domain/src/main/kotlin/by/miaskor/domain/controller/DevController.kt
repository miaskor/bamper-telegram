package by.miaskor.domain.controller

import by.miaskor.cloud.drive.domain.DownloadFile
import by.miaskor.cloud.drive.domain.UploadFileRequest
import by.miaskor.cloud.drive.service.ImageDownloader
import by.miaskor.cloud.drive.service.ImageUploader
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/dev")
class DevController(
  private val uploader: ImageUploader,
  private val imageDownloader: ImageDownloader
) {

  @PostMapping("/upload")
  fun upload(@RequestBody image: ByteArray): ResponseEntity<Unit> {
    uploader.upload(
      UploadFileRequest(
        1234,
        image,
        true,
        123
      )
    ).subscribe()

    return ResponseEntity.ok().build()
  }

  @PostMapping("/download")
  fun upload(@RequestBody downloadFile: DownloadFile): Mono<ResponseEntity<ByteArray>> {


    return imageDownloader.download(downloadFile)
      .map { ResponseEntity.ok(it) }
  }
}
