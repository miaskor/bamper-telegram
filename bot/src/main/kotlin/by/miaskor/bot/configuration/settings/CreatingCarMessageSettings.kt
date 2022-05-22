package by.miaskor.bot.configuration.settings

interface CreatingCarMessageSettings {
  fun brandNameMessage(): String
  fun invalidBrandNameMessage(): String
  fun modelMessage(): String
  fun invalidModelMessage(): String
  fun yearMessage(): String
  fun invalidYearMessage(): String
  fun bodyMessage(): String
  fun invalidBodyMessage(): String
  fun transmissionMessage(): String
  fun invalidTransmissionMessage(): String
  fun engineCapacityMessage(): String
  fun invalidEngineCapacityMessage(): String
  fun fuelTypeMessage(): String
  fun invalidFuelTypeMessage(): String
  fun engineTypeMessage(): String
  fun invalidEngineTypeMessage(): String
  fun completeCreatingMessage(): String
}
