package by.miaskor.bot.domain

import by.miaskor.bot.service.enrich.FieldEnrich

enum class FindingAutoPartStep(private vararg val values: String) : AbstractStep<FindingAutoPartStep> {
  @FieldEnrich("auto-part")
  AUTO_PART {
    override fun next() = AUTO_PART_NUMBER
    override fun previous() = AUTO_PART
  },

  @FieldEnrich("part-number")
  AUTO_PART_NUMBER {
    override fun next() = BRAND
    override fun previous() = AUTO_PART
  },

  @FieldEnrich("brand-name")
  BRAND {
    override fun next() = MODEL
    override fun previous() = AUTO_PART_NUMBER
  },

  @FieldEnrich("model")
  MODEL {
    override fun next() = CAR_YEAR
    override fun previous() = BRAND
  },

  @FieldEnrich("year")
  CAR_YEAR {
    override fun next() = COMPLETE
    override fun previous() = MODEL
  },
  COMPLETE {
    override fun next() = COMPLETE
    override fun previous() = COMPLETE
  };

  override fun isFinalStep() = this == COMPLETE

  override fun isStepNotMandatory() = true

  fun isAcceptable(value: String): Boolean {
    return this.values.firstOrNull { value.matches(Regex(it, RegexOption.IGNORE_CASE)) } != null
  }
}