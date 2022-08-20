package by.miaskor.configuration.settings

interface ImportSettings {

  fun headers(): List<String>
  fun fileSize(): Int
  fun importMediaType(): String
  fun importFileName(): String
}
