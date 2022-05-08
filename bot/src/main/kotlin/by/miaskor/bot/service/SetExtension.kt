package by.miaskor.bot.service

import java.util.*

fun <E> SortedSet<E>.pollLast(): E {
  val last = this.last()
  if(this.size >1) {
    this.remove(last)
  }
  return last
}
