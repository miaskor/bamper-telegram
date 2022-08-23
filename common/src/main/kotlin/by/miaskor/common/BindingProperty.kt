package by.miaskor.common

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class BindingProperty<V : Any>(
  private val bindPath: String,
) : Property<V>() {
  override fun propertyProvider(thisRef: Any, property: KProperty<*>): V {
    return configurationProvider.bind(bindPath, (property.returnType.classifier as KClass<V>).javaObjectType) as V
  }
}
