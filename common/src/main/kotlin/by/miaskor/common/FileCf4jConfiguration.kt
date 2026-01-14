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

private const val SEPARATOR = "|"
private const val FILE_PATH_KEY = "cf4j.properties.base-path"
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
    val basePathFromConfig = environment.getRequiredProperty(FILE_PATH_KEY)
    val files = environment.getRequiredProperty(FILES_KEY)

    val projectDir = Path.of(System.getProperty("user.dir"))
    val parentDir = projectDir.parent
      ?: throw IllegalStateException("Cannot resolve parent directory of project")

    val basePath = parentDir
      .resolve(basePathFromConfig)
      .normalize()
      .toAbsolutePath()

    return files.split(SEPARATOR)
      .map { fileName ->
        val resolvedName =
          if (fileName.matches(Regex(FILE_PROPERTY_PATTERN))) {
            fileName
          } else {
            "$fileName$DEFAULT_EXT"
          }

        basePath.resolve(resolvedName).normalize()
      }
  }
}
