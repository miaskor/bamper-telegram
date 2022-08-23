package by.miaskor.common

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

class DefaultProperty<V : Any>(
  private val propertyPath: String,
) : Property<V>() {
  override fun propertyProvider(thisRef: Any, property: KProperty<*>): V {
    return configurationProvider.getProperty(propertyPath, property.javaField?.type) as V
  }
}
