package by.miaskor.bot.configuration.settings

interface CreatingAutoPartMessageSettings {
  fun carMessage(): String
  fun carInvalidMessage(): String
  fun autoPartMessage(): String
  fun autoPartInvalidMessage(): String
  fun partNumberMessage(): String
  fun descriptionMessage(): String
  fun priceMessage(): String
  fun priceInvalidMessage(): String
  fun currencyMessage(): String
  fun currencyInvalidMessage(): String
  fun qualityMessage(): String
  fun qualityInvalidMessage(): String
  fun photoMessage(): String
  fun photoInvalidMessage(): String
  fun completeMessage(): String
}
