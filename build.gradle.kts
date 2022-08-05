import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript { plugins.apply("by.miaskor.artifact-versions") }
plugins {
  id("org.springframework.boot") version "2.6.6"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("nu.studer.jooq") version "6.0.1"
  id("org.liquibase.gradle") version "2.1.1"

  kotlin("jvm") version "1.6.10"
  kotlin("plugin.spring") version "1.6.10"
  kotlin("plugin.jpa") version "1.6.10"
  distribution
}

repositories {
  mavenCentral()
}

subprojects {
  apply {
    plugin("kotlin")
    plugin("org.springframework.boot")
    plugin("by.miaskor.artifact-versions")
    plugin("io.spring.dependency-management")
  }
  project(":domain:domain") {
    apply {
      plugin("nu.studer.jooq")
      plugin("org.liquibase.gradle")
      plugin("by.miaskor.mysql-credentials")
    }
  }

  group = "by.miaskor"
  version = "0.0.1-SNAPSHOT"
  java.sourceCompatibility = VERSION_11
  repositories {
    mavenCentral()
  }

  dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
      exclude(group = "org.springframework", module = "spring-webmvc")
    }
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(lib("org.cfg4j:cfg4j-core"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "11"
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

tasks.getByName("bootJar") {
  enabled = false
}
