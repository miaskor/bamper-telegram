package by.miaskor.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

open class VersionProvider {

  operator fun invoke(artifact: String): String {
    return "$artifact:${
      VersionService.mapVersion.get(artifact)
        ?: throw IllegalArgumentException("Artifact $artifact don't exists")
    }"
  }
}

class VersionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.extensions.create("lib", VersionProvider::class.java)
  }
}
