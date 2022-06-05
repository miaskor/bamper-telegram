package by.miaskor.bot.domain

enum class FindingAutoPartStep(private vararg val values: String) : AbstractStep<FindingAutoPartStep> {
  AUTO_PART_NUMBER {
    override fun next() = AUTO_PART
    override fun previous() = AUTO_PART_NUMBER
  },
  AUTO_PART {
    override fun next() = BRAND
    override fun previous() = AUTO_PART_NUMBER
  },
  BRAND {
    override fun next() = MODEL
    override fun previous() = AUTO_PART
  },
  MODEL {
    override fun next() = CAR_YEAR
    override fun previous() = BRAND
  },
  CAR_YEAR {
    override fun next() = CAR_YEAR
    override fun previous() = MODEL
  };

  override fun isFinalStep(): Boolean {
    TODO("Not yet implemented")
  }

  override fun isStepNotMandatory(): Boolean {
    TODO("Not yet implemented")
  }

  fun isAcceptable(value: String): Boolean {
    return this.values.firstOrNull { value.matches(Regex(it, RegexOption.IGNORE_CASE)) } != null
  }
}