package by.miaskor.service.excel

import by.miaskor.service.excel.domain.ExcelData
import by.miaskor.service.excel.domain.ExcelFileProperties
import org.dhatim.fastexcel.Workbook
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

interface ExcelGenerator {
  fun generate(
    excelData: ExcelData,
    excelFileProperties: ExcelFileProperties = ExcelFileProperties(),
  ): Mono<ByteArrayInputStream>
}

class DefaultExcelGenerator(private val workSheetEnricher: WorkSheetEnricher) : ExcelGenerator {

  override fun generate(excelData: ExcelData, excelFileProperties: ExcelFileProperties): Mono<ByteArrayInputStream> {
    return Mono.fromCallable { ByteArrayOutputStream(excelFileProperties.fileSize) }
      .map { file ->
        val workbook = Workbook(file, excelFileProperties.filename, excelFileProperties.applicationVersion)
        val worksheet = workbook.newWorksheet(excelFileProperties.sheetName)

        workSheetEnricher.enrich(worksheet, excelData)
        workbook.finish()
        ByteArrayInputStream(file.toByteArray())
      }
      .subscribeOn(Schedulers.boundedElastic())
  }
}
