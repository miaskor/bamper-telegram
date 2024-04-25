package by.miaskor.service.excel.domain

data class ExcelFileProperties(
  val filename: String = "New file",
  val applicationVersion: String = "1.0",
  val sheetName: String = "Sheet0",
  val fileSize: Int = 1024
)
