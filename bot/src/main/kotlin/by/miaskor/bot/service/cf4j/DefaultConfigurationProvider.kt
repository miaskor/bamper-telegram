package by.miaskor.bot.service.cf4j

import com.github.drapostolos.typeparser.NoSuchRegisteredParserException
import com.github.drapostolos.typeparser.TypeParser
import com.github.drapostolos.typeparser.TypeParserException
import org.cfg4j.provider.ConfigurationProvider
import org.cfg4j.provider.GenericTypeInterface
import org.cfg4j.source.ConfigurationSource
import org.cfg4j.source.context.environment.DefaultEnvironment
import org.cfg4j.source.context.environment.Environment
import org.cfg4j.source.context.environment.MissingEnvironmentException
import java.lang.reflect.Proxy
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DefaultConfigurationProvider(
  private val configurationSource: ConfigurationSource,
  private val environment: Environment = DefaultEnvironment(),
) : ConfigurationProvider {
  override fun allConfigurationAsProperties(): Properties {
    return try {
      configurationSource.getConfiguration(environment)
    } catch (e: IllegalStateException) {
      throw IllegalStateException("Couldn't fetch configuration from configuration source", e)
    } catch (e: MissingEnvironmentException) {
      throw IllegalStateException("Couldn't fetch configuration from configuration source", e)
    }
  }

  override fun <T : Any> getProperty(key: String, type: Class<T>): T {
    val propertyStr = getProperty(key)

    return try {
      val parser = TypeParser.newBuilder().build()
      parser.parse(propertyStr, type)
    } catch (e: TypeParserException) {
      throw IllegalArgumentException("Unable to cast value \'$propertyStr\' to $type", e)
    } catch (e: NoSuchRegisteredParserException) {
      throw IllegalArgumentException("Unable to cast value \'$propertyStr\' to $type", e)
    }
  }

  override fun <T : Any> getProperty(key: String, genericType: GenericTypeInterface): T {
    val propertyStr = getProperty(key)

    return try {
      val parser =
        TypeParser.newBuilder().build()
      parser.parseType(propertyStr, genericType.type) as T
    } catch (e: TypeParserException) {
      throw IllegalArgumentException("Unable to cast value \'$propertyStr\' to $genericType", e)
    } catch (e: NoSuchRegisteredParserException) {
      throw IllegalArgumentException("Unable to cast value \'$propertyStr\' to $genericType", e)
    }
  }

  override fun <T : Any> bind(prefix: String, type: Class<T>): T {
    val allProperties = getAllProperties(prefix, type)
    return bindObject(allProperties, type)
  }

  private fun <T : Any> bindObject(properties: Map<String, Any>, type: Class<T>): T {
    return Proxy.newProxyInstance(
      type.classLoader,
      arrayOf<Class<*>>(type)
    ) { proxy, method, methodArgs ->
      val any = properties[method.name]
      if (any is Map<*, *>) {
        bindObject(any as Map<String, Any>, method.returnType)
      } else {
        any
      }
    } as T
  }

  private fun <T : Any> getAllProperties(prefix: String, type: Class<T>): Map<String, Any> {
    val map = ConcurrentHashMap<String, Any>()

    type.declaredMethods.forEach {
      if (it.returnType.isInterface && it.returnType.simpleName.contains("Settings")) {
        map[it.name] = getAllProperties("$prefix.${it.name}", it.returnType)
      } else {
        map[it.name] = getProperty("$prefix.${it.name}", it.returnType)
      }
    }

    return map
  }

  private fun getProperty(key: String): String {
    return try {
      val property = configurationSource.getConfiguration(environment)[key]
        ?: throw NoSuchElementException("No configuration with key: $key")
      property.toString()
    } catch (e: IllegalStateException) {
      throw IllegalStateException("Couldn't fetch configuration from configuration source for key: $key", e)
    }
  }
}
