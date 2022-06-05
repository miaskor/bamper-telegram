package by.miaskor.bot.domain

import by.miaskor.bot.service.enrich.FieldEnrich

enum class CreatingCarStep(private vararg val values: String) : AbstractStep<CreatingCarStep> {
  @FieldEnrich("brand-name")
  BRAND_NAME {
    override fun next() = MODEL
    override fun previous() = BRAND_NAME
  },

  @FieldEnrich("model")
  MODEL {
    override fun next() = YEAR
    override fun previous() = BRAND_NAME
  },

  @FieldEnrich("year")
  YEAR {
    override fun next() = BODY
    override fun previous() = MODEL
  },

  @FieldEnrich("body")
  BODY {
    override fun next() = TRANSMISSION
    override fun previous() = YEAR
  },

  @FieldEnrich("transmission")
  TRANSMISSION {
    override fun next() = ENGINE_CAPACITY
    override fun previous() = BODY
  },

  @FieldEnrich("engine-capacity")
  ENGINE_CAPACITY {
    override fun next() = FUEL_TYPE
    override fun previous() = TRANSMISSION
  },

  @FieldEnrich("fuel-type")
  FUEL_TYPE {
    override fun next() = ENGINE_TYPE
    override fun previous() = ENGINE_CAPACITY
  },

  @FieldEnrich("engine-type")
  ENGINE_TYPE {
    override fun next() = COMPLETE

    override fun previous() = FUEL_TYPE
  },

  COMPLETE {
    override fun next() = COMPLETE
    override fun previous() = COMPLETE
  };

  override fun isFinalStep() = this == COMPLETE

  fun isAcceptable(value: String): Boolean {
    return this.values.firstOrNull { value.matches(Regex(it, RegexOption.IGNORE_CASE)) } != null
  }

  override fun isStepNotMandatory(): Boolean {
    return stepsWithMovementForward.contains(this)
  }

  private companion object {
    val stepsWithMovementForward = listOf(
      BODY, TRANSMISSION, ENGINE_CAPACITY, FUEL_TYPE, ENGINE_TYPE
    )
  }
}

