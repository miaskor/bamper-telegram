package by.miaskor.service.imprt

import by.miaskor.configuration.settings.ImportSettings
import by.miaskor.domain.api.domain.AutoPartResponse
import by.miaskor.service.excel.domain.ExcelData

class AutoPartResponseMapper(
  private val importSettings: ImportSettings,
) {

  fun map(autoPartResponse: AutoPartResponse): ExcelData {
    val values = listOf(
      mutableListOf<String>().apply {
        add("")
        add(autoPartResponse.brand)
        add(autoPartResponse.model)
        add(autoPartResponse.year)
        add(autoPartResponse.fuelType)
        add(autoPartResponse.engineCapacity.toString())
        add(autoPartResponse.engineType)
        add(autoPartResponse.transmission)
        add(autoPartResponse.body)
        add(autoPartResponse.autoPartName)
        add(autoPartResponse.description)
        add(autoPartResponse.partNumber)
        add(if (autoPartResponse.quality) "1" else "0")
        add(autoPartResponse.price.toString())
        add(autoPartResponse.currency)
        add(autoPartResponse.salePercent.toString())
        add(autoPartResponse.photoDownloadUrl)
      }
    )

    return ExcelData(importSettings.headers(), values)
  }
}