package by.miaskor.common

import org.cfg4j.provider.ConfigurationProvider
import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.files.FilesConfigurationSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.nio.file.Path
import java.util.regex.Pattern.matches
import kotlin.io.path.Path

private const val PROPERTY_DIR = "/bamper-telegram-properties"
private const val SEPARATOR = "|"
private const val FILE_PATH_KEY = "cf4j.properties.file-path"
private const val FILES_KEY = "cf4j.properties.files"
private const val DEFAULT_EXT = ".yaml"
private const val FILE_PROPERTY_PATTERN = ".+\\.\\w+"

@Configuration
open class FileCf4jConfiguration(
  private val environment: Environment,
) {

  @Bean
  open fun filesConfigurationSource(): FilesConfigurationSource {
    return FilesConfigurationSource(::getPropertyPaths)
  }

  @Bean
  open fun configurationProvider(): ConfigurationProvider {
    return ConfigurationProviderBuilder()
      .withConfigurationSource(filesConfigurationSource())
      .build()
  }

  protected fun getPropertyPaths(): List<Path> {
    val previousDirOfProject = environment.getProperty("user.dir")
      ?.substringBeforeLast("/")
    val basePath = environment.getProperty(FILE_PATH_KEY)
      ?: "$previousDirOfProject$PROPERTY_DIR"
    val files = environment.getProperty(FILES_KEY)
      ?: throw IllegalArgumentException("Property application.properties.files is not exists")

    return files.split(SEPARATOR)
      .map { fileName ->
        if (matches(FILE_PROPERTY_PATTERN, fileName)) {
          "$basePath/$fileName"
        } else {
          "$basePath/$fileName$DEFAULT_EXT"
        }
      }
      .map(::Path)
  }
}
