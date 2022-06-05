package by.miaskor.bot.domain

import by.miaskor.domain.api.domain.AutoPartDto

class AutoPartBuilder(
  override var step: CreatingAutoPartStep = CreatingAutoPartStep.CAR
) : AbstractStepBuilder<CreatingAutoPartStep>() {
  private lateinit var carIdValue: String
  private lateinit var carPartIdValue: String
  private lateinit var descriptionValue: String
  private lateinit var priceValue: String
  private lateinit var qualityValue: String
  private lateinit var currencyValue: String
  private lateinit var partNumberValue: String
  private lateinit var photoIdValue: String

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

  fun photoId(photoId: String): AutoPartBuilder {
    this.photoIdValue = photoId
    return this
  }

  fun build(storeHouseId: Long, chatId: Long): AutoPartDto {
    return AutoPartDto(
      carId = carIdValue.toLong(),
      carPartId = carPartIdValue.toLong(),
      storeHouseId = storeHouseId,
      description = if (::descriptionValue.isInitialized) descriptionValue else "",
      price = if (::priceValue.isInitialized) priceValue.toDouble() else 0.0,
      quality = if (::qualityValue.isInitialized) qualityValue.toBoolean() else false,
      currency = if (::currencyValue.isInitialized) currencyValue else "BYN",
      partNumber = if (::partNumberValue.isInitialized) partNumberValue else "",
      photoId = photoIdValue,
      chatId = chatId
    )
  }
}
