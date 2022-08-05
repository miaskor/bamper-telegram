package by.miaskor.plugin

interface DataSourceSettings {

  fun driverClassName(): String
  fun username(): String
  fun password(): String
  fun url(): String
}
