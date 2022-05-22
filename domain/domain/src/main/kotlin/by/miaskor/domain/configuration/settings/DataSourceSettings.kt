package by.miaskor.domain.configuration.settings

interface DataSourceSettings {

  fun driverClassName(): String
  fun username(): String
  fun password(): String
  fun url(): String
}
