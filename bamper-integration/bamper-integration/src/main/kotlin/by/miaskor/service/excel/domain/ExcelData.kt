package by.miaskor.service.excel.domain

data class ExcelData(
  val headers: List<String>,
  val values: List<List<String>>,
)