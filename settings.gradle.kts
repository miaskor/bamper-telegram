pluginManagement {
  plugins {

    //todo(refactoring)
    val versions = File("/home/miaskor/IdeaProjects/bamper-telegram-properties/versions.yaml")
      .absoluteFile
      .readLines()

    fun getPluginVersion(versionForPlugin: String): String {
      return versions.find { plugin -> plugin.contains("plugin.$versionForPlugin: ") }
        ?.substringAfter("plugin.$versionForPlugin: ")
        ?: throw IllegalArgumentException("Plugin $versionForPlugin not found")
    }

    id("org.springframework.boot") version getPluginVersion("org.springframework.boot")
    id("io.spring.dependency-management") version getPluginVersion("io.spring.dependency-management")
    id("nu.studer.jooq") version getPluginVersion("nu.studer.jooq")
    id("org.liquibase.gradle") version getPluginVersion("org.liquibase.gradle")

    kotlin("jvm") version getPluginVersion("kotlin.jvm")
    kotlin("plugin.spring") version getPluginVersion("kotlin.plugin.spring")
    kotlin("plugin.jpa") version getPluginVersion("kotlin.plugin.jpa")

  }
}
rootProject.name = "bamper-telegram"
include("bot")
include("domain:domain-api")
include("domain:domain")
include("cloud-drive")
include("bamper-integration:bamper-integration")
include("bamper-integration:bamper-integration-api")
include("common")
