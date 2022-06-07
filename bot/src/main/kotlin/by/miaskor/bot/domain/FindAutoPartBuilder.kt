package by.miaskor.bot.domain

import by.miaskor.bot.domain.FindingAutoPartStep.AUTO_PART_NUMBER
import by.miaskor.domain.api.domain.FindAutoPartDto

class FindAutoPartBuilder(
  override var step: FindingAutoPartStep = AUTO_PART_NUMBER
) : AbstractStepBuilder<FindingAutoPartStep>() {
  private lateinit var partNumberValue: String
  private lateinit var modelValue: String
  private lateinit var brandValue: String
  private lateinit var yearValue: String
  private lateinit var autoPartId: String

  fun partNumber(partNumber: String): FindAutoPartBuilder {
    this.partNumberValue = partNumber
    return this
  }

  fun model(model: String): FindAutoPartBuilder {
    this.modelValue = model
    return this
  }

  fun brand(brand: String): FindAutoPartBuilder {
    this.brandValue = brand
    return this
  }

  fun year(year: String): FindAutoPartBuilder {
    this.yearValue = year
    return this
  }

  fun autoPart(autoPart: String): FindAutoPartBuilder {
    this.autoPartId = autoPart
    return this
  }

  fun isEmpty(): Boolean {
    return !(::partNumberValue.isInitialized
        && ::brandValue.isInitialized
        && ::modelValue.isInitialized
        && ::yearValue.isInitialized
        && ::autoPartId.isInitialized)
  }

  fun build(storeHouseId: Long): FindAutoPartDto {
    return FindAutoPartDto(
      partNumber = if (::partNumberValue.isInitialized) partNumberValue else "",
      brand = if (::brandValue.isInitialized) brandValue else "",
      model = if (::modelValue.isInitialized) modelValue else "",
      year = if (::yearValue.isInitialized) yearValue else "",
      autoPartId = if (::autoPartId.isInitialized) autoPartId else "",
      storeHouseId = storeHouseId
    )
  }
}