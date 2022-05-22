package by.miaskor.bot.service.enrich

import org.cfg4j.provider.ConfigurationProvider
import java.lang.reflect.Field

object FieldEnricher {

  lateinit var configurationProvider: ConfigurationProvider

  fun enrich(clazz: Class<*>) {
    clazz.declaredFields.forEach { field ->
      if (field.isEnumConstant) {
        enrichField(field, clazz)
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
      val actualField = clazz.declaredFields.firstOrNull { it.name == fieldEnrich.field }
      actualField?.isAccessible = true
      actualField?.set(enumField.get(null), values)
    }
  }
}



