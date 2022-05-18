package by.miaskor.bot.domain

enum class CreatingCarStep(private vararg val values: String) : AbstractStep<CreatingCarStep> {
  BRAND_NAME("[a-zA-Z\\s]+\$") {
    override fun next() = MODEL
    override fun previous() = BRAND_NAME
  },
  MODEL(".*") {
    override fun next() = YEAR
    override fun previous() = BRAND_NAME
  },
  YEAR("199[2-9]", "20[0-2][0-9]") {
    override fun next() = BODY
    override fun previous() = MODEL
  },
  BODY(
    "седан", "хэтчбек", "универсал", "купе", "кабриолет", "бортовой",
    "минивэн", "пикап", "фургон", "тягач", "лифтбек", "внедорожник",
    "sedan", "hatchback", "universal", "compartment", "convertible",
    "onboard", "minivan", "pickup", "van", "tractor", "liftback", "suv"
  ) {
    override fun next() = TRANSMISSION
    override fun previous() = YEAR
  },
  TRANSMISSION(
    "АКПП", "МКПП", "Робот", "Вариатор",
    "Automatic", "Manual", "Robot", "Variator"
  ) {
    override fun next() = ENGINE_CAPACITY
    override fun previous() = BODY
  },
  ENGINE_CAPACITY("\\d.\\d") {
    override fun next() = FUEL_TYPE
    override fun previous() = TRANSMISSION
  },
  FUEL_TYPE(
    "Бензин", "Дизель", "Гибрид", "Электро",
    "Gas", "Diesel", "Hybrid", "Electro",
  ) {
    override fun next() = ENGINE_TYPE
    override fun previous() = ENGINE_CAPACITY
  },
  ENGINE_TYPE(
    "EcoBoost", "FSI", "GDi", "HPi", "i", "i-VTEC", "IDE", "JTS",
    "Kompr", "MPI", "TFSI", "TSI", "VTEC", "VTI", "VVT-i", "карб",
    "моно", "carb", "mono"
  ) {
    override fun next(): CreatingCarStep {
      isComplete = true
      return ENGINE_TYPE
    }

    override fun previous() = FUEL_TYPE
  };

  var isComplete = false

  fun isAcceptable(value: String): Boolean {
    return this.values.firstOrNull { value.matches(Regex(it, RegexOption.IGNORE_CASE)) } != null
  }

  companion object {
    val stepsWithMovementForward = listOf(
      BODY, TRANSMISSION, ENGINE_CAPACITY, FUEL_TYPE, ENGINE_TYPE
    )
  }
}

