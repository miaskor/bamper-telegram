package by.miaskor.configuration.settings

interface BamperSettings {

  fun bamperUrl(): String
  fun authorizationUri(): String
  fun importUri(): String
  fun parseUri(): String
  fun authorizationFormData(): List<String>
}
