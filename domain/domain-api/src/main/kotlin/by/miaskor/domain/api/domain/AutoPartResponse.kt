package by.miaskor.domain.api.domain

data class AutoPartResponse(
  val description: String,
  val photo: ByteArray,
  val price: Double,
  val quality: Boolean,
  val currency: String,
  val partNumber: String,
  val model: String,
  val brand: String,
  val year: String,
  val autoPartEN: String,
  val autoPartRU: String
) {
  companion object {
    fun disassembly(autoPartResponse: AutoPartResponse, language: String): Array<String> {
      return autoPartResponse.let {
        arrayOf(
          if (language == "EN") it.autoPartEN else it.autoPartRU,
          it.brand,
          it.model,
          it.year,
          it.description,
          it.partNumber,
          it.price.toString(),
          it.currency,
          it.quality.toString()
        )
      }
    }
  }
}

