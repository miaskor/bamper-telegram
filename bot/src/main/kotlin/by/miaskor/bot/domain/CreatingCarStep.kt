package by.miaskor.bot.domain

import by.miaskor.bot.service.enrich.FieldEnrich

enum class CreatingCarStep(private vararg val values: String) : AbstractStep<CreatingCarStep> {
  @FieldEnrich("brand-name", "values")
  BRAND_NAME {
    override fun next() = MODEL
    override fun previous() = BRAND_NAME
  },

  @FieldEnrich("model", "values")
  MODEL {
    override fun next() = YEAR
    override fun previous() = BRAND_NAME
  },

  @FieldEnrich("year", "values")
  YEAR {
    override fun next() = BODY
    override fun previous() = MODEL
  },

  @FieldEnrich("body", "values")
  BODY {
    override fun next() = TRANSMISSION
    override fun previous() = YEAR
  },

  @FieldEnrich("transmission", "values")
  TRANSMISSION {
    override fun next() = ENGINE_CAPACITY
    override fun previous() = BODY
  },

  @FieldEnrich("engine-capacity", "values")
  ENGINE_CAPACITY {
    override fun next() = FUEL_TYPE
    override fun previous() = TRANSMISSION
  },

  @FieldEnrich("fuel-type", "values")
  FUEL_TYPE {
    override fun next() = ENGINE_TYPE
    override fun previous() = ENGINE_CAPACITY
  },

  @FieldEnrich("engine-type", "values")
  ENGINE_TYPE {
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

