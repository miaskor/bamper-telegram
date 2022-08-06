package by.miaskor.service

import by.miaskor.configuration.settings.ImportSettings
import by.miaskor.domain.AdvertisementDto
import org.dhatim.fastexcel.Workbook
import reactor.core.publisher.Mono
import java.io.FileOutputStream

class ExcelGenerator(
  private val importSettings: ImportSettings,
) {

  fun generate(advertisementDto: List<AdvertisementDto>): Mono<Unit> {
    return Mono.fromSupplier {
      val wb = Workbook(FileOutputStream("Test.xls"), "MyApplication", "1.0")
      val ws = wb.newWorksheet("Sheet 1")
      val headers = importSettings.headers()
      headers.forEachIndexed { index, s ->
        ws.value(0, index, s)
      }

      advertisementDto.forEachIndexed { index, s ->
        ws.value(index + 1, 1, s.brand)
        ws.value(index + 1, 2, s.model)
        ws.value(index + 1, 4, s.year)
        ws.value(index + 1, 5, s.fuelType)
        ws.value(index + 1, 6, s.engineType)
        ws.value(index + 1, 7, s.transmission)
        ws.value(index + 1, 8, s.body)
        ws.value(index + 1, 9, s.autoPartName)
        ws.value(index + 1, 10, s.partNumber)
        ws.value(index + 1, 12, s.quality)
        ws.value(index + 1, 20, s.price)
        ws.value(index + 1, 21, s.currency)
        ws.value(index + 1, 22, s.salePercent)
        ws.value(index + 1, 23, s.photoDownloadUrl)
      }
      wb.finish()
    }
  }
}
