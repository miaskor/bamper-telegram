package by.miaskor.service

import by.miaskor.configuration.settings.ImportSettings
import by.miaskor.domain.api.domain.AutoPartResponse
import org.dhatim.fastexcel.Workbook
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ExcelGenerator(
  private val importSettings: ImportSettings,
) {

  fun generate(autoPartResponse: AutoPartResponse): Mono<ByteArrayInputStream> {
    return Mono.fromCallable { ByteArrayOutputStream(10_000) }
      .map { fos ->
        val wb = Workbook(fos, "MyApplication", "1.0")
        val ws = wb.newWorksheet("Sheet 1")
        val headers = importSettings.headers()
        headers.forEachIndexed { index, s ->
          ws.value(0, index, s)
        }

        ws.apply {
          value(1, 0, 123)
          value(1, 1, autoPartResponse.brand)
          value(1, 2, autoPartResponse.model)
          value(1, 4, autoPartResponse.year)
          value(1, 5, autoPartResponse.fuelType)
          value(1, 6, autoPartResponse.engineCapacity)
          value(1, 7, autoPartResponse.engineType)
          value(1, 8, autoPartResponse.transmission)
          value(1, 9, autoPartResponse.body)
          value(1, 10, autoPartResponse.autoPartName)
          value(1, 11, autoPartResponse.description)
          value(1, 12, autoPartResponse.partNumber)
          value(1, 14, if(autoPartResponse.quality) 1 else 0)
          value(1, 22, autoPartResponse.price)
          value(1, 23, autoPartResponse.currency)
          value(1, 24, autoPartResponse.salePercent)
          value(1, 25, autoPartResponse.photoDownloadUrl)
        }
        wb.finish()
        ByteArrayInputStream(fos.toByteArray())
      }.subscribeOn(Schedulers.boundedElastic())
  }
}
