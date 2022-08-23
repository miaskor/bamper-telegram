package by.miaskor.service

import by.miaskor.configuration.settings.ImportSettings
import by.miaskor.domain.api.domain.AutoPartResponse
import org.dhatim.fastexcel.Workbook
import org.dhatim.fastexcel.Worksheet
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ExcelGenerator(
  private val importSettings: ImportSettings,
) {

  fun generate(autoPartResponse: AutoPartResponse): Mono<ByteArrayInputStream> {
    return Mono.fromCallable { ByteArrayOutputStream(importSettings.fileSize()) }
      .map { file ->
        val workbook = Workbook(file, FILE_NAME, APPLICATION_VERSION)
        val worksheet = workbook.newWorksheet(SHEET_NAME)

        fillHeaders(worksheet)
        fillValues(worksheet = worksheet, autoPartResponse = autoPartResponse)
        workbook.finish()
        ByteArrayInputStream(file.toByteArray())
      }.subscribeOn(Schedulers.boundedElastic())
  }

  private fun fillHeaders(worksheet: Worksheet) {
    val headers = importSettings.headers()
    headers.forEachIndexed { index, s ->
      worksheet.value(HEADER_ROW, index, s)
    }
  }

  private fun fillValues(row: Int = 1, worksheet: Worksheet, autoPartResponse: AutoPartResponse) {
    worksheet.apply {
      value(row, 1, autoPartResponse.brand)
      value(row, 2, autoPartResponse.model)
      value(row, 4, autoPartResponse.year)
      value(row, 5, autoPartResponse.fuelType)
      value(row, 6, autoPartResponse.engineCapacity)
      value(row, 7, autoPartResponse.engineType)
      value(row, 8, autoPartResponse.transmission)
      value(row, 9, autoPartResponse.body)
      value(row, 10, autoPartResponse.autoPartName)
      value(row, 11, autoPartResponse.description)
      value(row, 12, autoPartResponse.partNumber)
      value(row, 14, if (autoPartResponse.quality) 1 else 0)
      value(row, 22, autoPartResponse.price)
      value(row, 23, autoPartResponse.currency)
      value(row, 24, autoPartResponse.salePercent)
      value(row, 25, autoPartResponse.photoDownloadUrl)
    }
  }

  private companion object {
    private const val HEADER_ROW = 0
    private const val FILE_NAME = "auto_part_advertisement"
    private const val APPLICATION_VERSION = "1.0"
    private const val SHEET_NAME = "advertisement"
  }
}
