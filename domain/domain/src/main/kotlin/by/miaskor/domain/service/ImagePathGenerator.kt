package by.miaskor.domain.service

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ImagePathGenerator {

  private val timePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH'%3A'mm'%3A'ss")

  fun generate(chatId: String, autoPartKey: String): String {
    val now = LocalDateTime.now().format(timePattern)
    return "${chatId}/${autoPartKey}-${now}"
  }
}
