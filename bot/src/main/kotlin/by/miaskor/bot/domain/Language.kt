package by.miaskor.bot.domain

enum class Language(val domain: String) {
  ENGLISH("EN"),
  RUSSIAN("RU");

  companion object {
    fun findByDomain(domain: String): Language {
      return values().find { it.domain == domain } ?: ENGLISH
    }

    fun getDomainByFullLanguage(fullLanguage: String): Language {
      return when (fullLanguage) {
        "English" -> ENGLISH
        else -> RUSSIAN
      }
    }
  }
}
