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
  override fun toString(): String {
    return "AutoPartResponse(description='$description', price=$price," +
        " quality=$quality, currency='$currency', partNumber='$partNumber'," +
        " model='$model', brand='$brand', year='$year'," +
        " autoPartEN='$autoPartEN', autoPartRU='$autoPartRU')"
  }
}

