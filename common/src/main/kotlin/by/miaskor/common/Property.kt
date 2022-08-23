package by.miaskor.common

import org.cfg4j.provider.ConfigurationProvider
import java.util.concurrent.atomic.AtomicReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Property<V : Any> : ReadOnlyProperty<Any, V> {

  private val backField = AtomicReference<V>()

  override fun getValue(thisRef: Any, property: KProperty<*>): V {
    return if (backField.get() == null) {
      backField.updateAndGet { propertyProvider(thisRef, property) }
    } else {
      backField.get()
    }
  }

  protected abstract fun propertyProvider(thisRef: Any, property: KProperty<*>): V

  companion object {
    lateinit var configurationProvider: ConfigurationProvider
  }
}



