package by.miaskor.bot.domain

import by.miaskor.bot.service.enrich.FieldEnrich

enum class CreatingAutoPartStep(private vararg val values: String) : AbstractStep<CreatingAutoPartStep> {

  @FieldEnrich("car")
  CAR {
    override fun next() = AUTO_PART
    override fun previous() = CAR
  },

  @FieldEnrich("auto-part")
  AUTO_PART {
    override fun next() = PART_NUMBER
    override fun previous() = CAR
  },

  @FieldEnrich("part-number")
  PART_NUMBER {
    override fun next() = DESCRIPTION
    override fun previous() = AUTO_PART
  },

  @FieldEnrich("description")
  DESCRIPTION {
    override fun next() = PRICE
    override fun previous() = PART_NUMBER
  },

  @FieldEnrich("price")
  PRICE {
    override fun next() = CURRENCY
    override fun previous() = DESCRIPTION
  },

  @FieldEnrich("currency")
  CURRENCY {
    override fun next() = QUALITY
    override fun previous() = PRICE
  },

  @FieldEnrich("quality")
  QUALITY {
    override fun next() = PHOTO
    override fun previous() = PRICE
  },

  PHOTO("") {
    override fun next() = COMPLETE
    override fun previous() = QUALITY
  },

  COMPLETE {
    override fun next() = COMPLETE
    override fun previous() = COMPLETE
  };

  override fun isFinalStep() = this == COMPLETE

  override fun isStepNotMandatory() = stepsWithMovementForward.contains(this)

  fun isAcceptable(value: String): Boolean {
    return this.values.firstOrNull { value.matches(Regex(it, RegexOption.IGNORE_CASE)) } != null
  }

  private companion object {
    private val stepsWithMovementForward = listOf(
      PART_NUMBER,
      DESCRIPTION,
      PRICE,
      QUALITY,
      CURRENCY
    )
  }
}
