package by.miaskor.bot.service.cf4j

import org.cfg4j.source.context.propertiesprovider.PropertiesProvider
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.reader.UnicodeReader
import org.yaml.snakeyaml.scanner.ScannerException
import java.io.IOException
import java.io.InputStream
import java.util.*

private const val LINK_PATTERN = "#link<.*>"
private const val SEPARATOR = ","

class LinkPropertiesProvider : PropertiesProvider {

  private val inMemoryListProperties = mutableListOf<Properties>()

  override fun getProperties(inputStream: InputStream?): Properties {
    val yaml = Yaml()
    val properties = Properties()
    try {
      UnicodeReader(inputStream).use { reader ->
        val `object` = yaml.load<Any>(reader)
        if (`object` != null) {
          val yamlAsMap = convertToMap(`object`)
          val flatten = flatten(yamlAsMap).toMutableMap()
          for (key in flatten.keys) {
            var value = flatten[key]
            if (value is String && value.matches(Regex(LINK_PATTERN))) {
              val actualKeys = removeLinkPattern(value)
              value = actualKeys.map { actualKey ->
                inMemoryListProperties.first {
                  it.containsKey(actualKey)
                }.getValue(actualKey)
              }
                .joinToString(SEPARATOR)
              flatten[key] = value
            }
          }
          properties.putAll(flatten)
        }
        inMemoryListProperties.add(properties)
        return properties
      }
    } catch (e: IOException) {
      throw IllegalStateException("Unable to load yaml configuration from provided stream", e)
    } catch (e: ScannerException) {
      throw IllegalStateException("Unable to load yaml configuration from provided stream", e)
    }
  }

  private fun removeLinkPattern(value: String): List<String> {
    return value
      .replace(LINK_PATTERN.substringBefore("."), "")
      .replace(LINK_PATTERN.last().toString(), "")
      .split(SEPARATOR)
  }

  /**
   * Convert given Yaml document to a multi-level map.
   */
  private fun convertToMap(yamlDocument: Any?): Map<String, Any?> {
    val yamlMap: MutableMap<String, Any?> = LinkedHashMap()

    // Document is a text block
    if (yamlDocument !is Map<*, *>) {
      yamlMap["content"] = yamlDocument
      return yamlMap
    }
    for (entry in yamlDocument.entries) {
      var value = entry.value
      if (value is Map<*, *>) {
        value = convertToMap(value)
      } else if (value is Collection<*>) {
        val collection = ArrayList<Map<String, Any>>()
        for (element in value) {
          collection.add(convertToMap(element) as Map<String, Any>)
        }
        value = collection
      }

      yamlMap[entry.key.toString()] = value
    }
    return yamlMap
  }

  private fun flatten(source: Map<String, Any?>): Map<String, Any?> {
    val result: MutableMap<String, Any?> = LinkedHashMap()
    for (key in source.keys) {
      val value = source[key]
      if (value is Map<*, *>) {
        val subMap = flatten(value as Map<String, Any?>)
        for (subkey in subMap.keys) {
          result["$key.$subkey"] = subMap[subkey]
        }
      } else if (value is Collection<*>) {
        val joiner = StringBuilder()
        var separator = ""
        for (element in value) {
          val subMap = flatten(Collections.singletonMap(key, element))
          joiner
            .append(separator)
            .append(subMap.entries.iterator().next().value.toString())
          separator = SEPARATOR
        }
        result[key] = joiner.toString()
      } else {
        result[key] = value
      }
    }
    return result
  }
}

