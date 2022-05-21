package by.miaskor.bot.service.enrich

import org.cfg4j.provider.ConfigurationProvider
import java.lang.reflect.Field

object FieldEnricher {

  lateinit var configurationProvider: ConfigurationProvider

  fun enrichClass(clazz: Class<*>) {
    clazz.declaredFields.forEach {
      if (it.isEnumConstant) {
        enrichField(it, clazz)
      }
    }
  }

  private fun enrichField(enumField: Field, clazz: Class<*>) {
    if (enumField.isAnnotationPresent(FieldEnrich::class.java)) {
      val fieldEnrich = enumField.getAnnotation(FieldEnrich::class.java)
      val value = configurationProvider.getProperty(fieldEnrich.property, String::class.java)
      val values: Array<String> = if (value.contains(Regex("[{}]"))) {
        arrayOf(value)
      } else {
        value.split(",").toTypedArray()
      }
      val field = clazz.declaredFields.firstOrNull { it.name == fieldEnrich.field }
      field?.isAccessible = true
      field?.set(enumField.get(null), values)
    }
  }
}



