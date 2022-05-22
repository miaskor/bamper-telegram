package by.miaskor.bot.domain

import by.miaskor.domain.api.domain.AutoPartDto

class AutoPartBuilder {
  private lateinit var carIdValue: String
  private lateinit var carPartIdValue: String
  private lateinit var descriptionValue: String
  private lateinit var priceValue: String
  private lateinit var qualityValue: String
  private lateinit var currencyValue: String
  private lateinit var partNumberValue: String

  private var autoPartStep: CreatingAutoPartStep = CreatingAutoPartStep.CAR

  fun carId(carId: Long): AutoPartBuilder {
    this.carIdValue = carId.toString()
    return this
  }

  fun carPartId(carPartId: Long): AutoPartBuilder {
    this.carPartIdValue = carPartId.toString()
    return this
  }

  fun description(description: String): AutoPartBuilder {
    this.descriptionValue = description
    return this
  }

  fun price(price: String): AutoPartBuilder {
    this.priceValue = price
    return this
  }

  fun quality(quality: String): AutoPartBuilder {
    this.qualityValue = quality
    return this
  }

  fun currency(currency: String): AutoPartBuilder {
    this.currencyValue = currency
    return this
  }

  fun partNumber(partNumber: String): AutoPartBuilder {
    this.partNumberValue = partNumber
    return this
  }

  fun nextStep(): AutoPartBuilder {
    this.autoPartStep = autoPartStep.next()
    return this
  }

  fun currentStep(): CreatingAutoPartStep {
    return this.autoPartStep
  }

  fun previousStep(): AutoPartBuilder {
    this.autoPartStep = autoPartStep.previous()
    return this
  }

  fun build(storeHouseName: String, photo: ByteArray): AutoPartDto {
    return AutoPartDto(
      carId = carIdValue.toLong(),
      carPartId = carPartIdValue.toLong(),
      storeHouseName = storeHouseName,
      description = if (::descriptionValue.isInitialized) descriptionValue else "",
      price = if (::priceValue.isInitialized) priceValue.toDouble() else 0.0,
      quality = if (::qualityValue.isInitialized) qualityValue.toBoolean() else false,
      currency = if (::currencyValue.isInitialized) currencyValue else "",
      partNumber = if (::partNumberValue.isInitialized) partNumberValue else "",
      photo = photo
    )
  }
}
