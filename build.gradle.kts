import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.6.6"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("nu.studer.jooq") version "6.0.1"
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
    plugin("io.spring.dependency-management")
  }
  project(":domain") {
    apply {
      plugin("nu.studer.jooq")
    }
  }

  group = "by.miaskor"
  version = "0.0.1-SNAPSHOT"
  java.sourceCompatibility = VERSION_11
  repositories {
    mavenCentral()
  }

  dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.cfg4j:cfg4j-core:4.4.1")
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
