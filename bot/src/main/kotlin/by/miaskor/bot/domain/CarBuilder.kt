package by.miaskor.bot.domain

import by.miaskor.domain.api.domain.CarDto

class CarBuilder(
  override var step: CreatingCarStep = CreatingCarStep.BRAND_NAME
) : AbstractStepBuilder<CreatingCarStep>() {
  private lateinit var brandIdValue: String
  private lateinit var brandNameValue: String
  private lateinit var modelValue: String
  private lateinit var yearValue: String
  private lateinit var bodyValue: String
  private lateinit var transmissionValue: String
  private lateinit var engineCapacityValue: String
  private lateinit var fuelTypeValue: String
  private lateinit var engineTypeValue: String

  fun brandId(brandId: Long): CarBuilder {
    this.brandIdValue = brandId.toString()
    return this
  }

  fun brandName(brandName: String): CarBuilder {
    this.brandNameValue = brandName
    return this
  }

  fun model(model: String): CarBuilder {
    this.modelValue = model
    return this
  }

  fun year(year: String): CarBuilder {
    this.yearValue = year
    return this
  }

  fun body(body: String): CarBuilder {
    this.bodyValue = body
    return this
  }

  fun transmission(transmission: String): CarBuilder {
    this.transmissionValue = transmission
    return this
  }

  fun engineCapacity(engineCapacity: String): CarBuilder {
    this.engineCapacityValue = engineCapacity
    return this
  }

  fun fuelType(fuelType: String): CarBuilder {
    this.fuelTypeValue = fuelType
    return this
  }

  fun engineType(engineType: String): CarBuilder {
    this.engineTypeValue = engineType
    return this
  }

  fun getBrandName(): String {
    return if (::brandNameValue.isInitialized) brandNameValue else ""
  }

  fun build(storeHouseId: Long): CarDto {
    return CarDto(
      brandId = brandIdValue.toLong(),
      storeHouseId = storeHouseId,
      model = modelValue,
      year = yearValue,
      body = if (::bodyValue.isInitialized) bodyValue else "",
      transmission = if (::transmissionValue.isInitialized) transmissionValue else "",
      engineCapacity = if (::engineCapacityValue.isInitialized) engineCapacityValue.toDouble() else 0.0,
      fuelType = if (::fuelTypeValue.isInitialized) fuelTypeValue else "",
      engineType = if (::engineTypeValue.isInitialized) engineTypeValue else "",
    )
  }
}
