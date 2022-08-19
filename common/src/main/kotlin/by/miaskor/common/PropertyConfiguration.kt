package by.miaskor.common

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.nio.file.Path
import kotlin.io.path.Path

@Configuration
open class PropertyConfiguration(
  private val environment: Environment,
) {

  fun getPropertyPaths(): List<Path> {
    val basePath = environment.getProperty("application.properties.base-path")
    val files = environment.getProperty("application.properties.files")
      ?: throw IllegalArgumentException("Property application.properties.files is not exists")
    val fileExtension = ".yaml"

    return files.split("|")
      .map { propertyPath -> "$basePath/$propertyPath$fileExtension" }
      .map { propertyPath -> Path(propertyPath) }
  }
}
