package by.miaskor.bot.domain

import by.miaskor.domain.api.domain.FindAutoPartDto

class FindAutoPartBuilder {
  private lateinit var partNumberValue: String
  private lateinit var modelValue: String
  private lateinit var brandValue: String
  private lateinit var yearValue: String
  private lateinit var autoPartValue: String

  private var findingAutoPartStep: FindingAutoPartStep = FindingAutoPartStep.AUTO_PART_NUMBER

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
    this.autoPartValue = autoPart
    return this
  }

  fun nextStep(): FindAutoPartBuilder {
    this.findingAutoPartStep = findingAutoPartStep.next()
    return this
  }

  fun currentStep(): FindingAutoPartStep {
    return this.findingAutoPartStep
  }

  fun previousStep(): FindAutoPartBuilder {
    this.findingAutoPartStep = findingAutoPartStep.previous()
    return this
  }

  fun build(storeHouseId: Long): FindAutoPartDto {
    return FindAutoPartDto(
      partNumber = if (::partNumberValue.isInitialized) partNumberValue else "",
      brand = if (::brandValue.isInitialized) brandValue else "",
      model = if (::modelValue.isInitialized) modelValue else "",
      year = if (::yearValue.isInitialized) yearValue else "",
      autoPart = if (::autoPartValue.isInitialized) autoPartValue else "",
      storeHouseId = storeHouseId
    )
  }
}