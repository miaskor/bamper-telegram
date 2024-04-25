package by.miaskor.service.excel

import by.miaskor.service.excel.domain.ExcelData
import org.dhatim.fastexcel.Worksheet

interface WorkSheetEnricher {

  fun enrich(worksheet: Worksheet, excelData: ExcelData)
}

private const val HEADER_ROW = 0

class DefaultWorkSheetEnricher : WorkSheetEnricher {

  override fun enrich(worksheet: Worksheet, excelData: ExcelData) {
    fillHeaders(worksheet, excelData.headers)
    fillValues(worksheet, excelData.values)
  }

  private fun fillHeaders(worksheet: Worksheet, headers: List<String>) {
    headers.forEachIndexed { index, s ->
      worksheet.value(HEADER_ROW, index, s)
    }
  }

  private fun fillValues(worksheet: Worksheet, values: List<List<String>>) {
    worksheet.apply {
      values.forEachIndexed { indexRow, rowValues ->
        rowValues.forEachIndexed { indexColumn, value ->
          value(indexRow + 1, indexColumn, value)
        }
      }
    }
  }
}