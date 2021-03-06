package by.miaskor.bot.domain

import by.miaskor.domain.api.domain.ConstraintType

data class ConstraintAutoPartListEntity(
  val constraint: String = "",
  override val reachLimit: Boolean = false,
  override val offset: Long = 0,
  val constraintType: ConstraintType,
) : ListEntity(offset, reachLimit)
