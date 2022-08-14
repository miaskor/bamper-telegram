package by.miaskor.plugin

object VersionService {

  val mapVersion = mutableMapOf<String, String>()

  private val versions: Map<List<String>, String> = mapOf(
    listOf("org.cfg4j:cfg4j-core") to "4.4.1",
    listOf("com.github.pengrad:java-telegram-bot-api") to "6.0.0",
    listOf("io.projectreactor:reactor-core") to "3.4.17",
    listOf("com.github.ben-manes.caffeine:caffeine") to "3.1.0",
    listOf("org.jetbrains.kotlin:kotlin-reflect") to "1.6.21",
    listOf("io.projectreactor.netty:reactor-netty") to "1.0.18",
    listOf("io.projectreactor.kotlin:reactor-kotlin-extensions") to "1.1.6",
    listOf(
      "org.jooq:jooq-meta",
      "org.jooq:jooq-codegen"
    ) to "3.15.1",
    listOf("mysql:mysql-connector-java") to "8.0.18",
    listOf("org.liquibase:liquibase-core") to "4.5.0",
    listOf("org.liquibase:liquibase-groovy-dsl") to "3.0.0",
    listOf("info.picocli:picocli") to "4.6.1",
    listOf("org.yaml:snakeyaml") to "1.29",
    listOf("org.dhatim:fastexcel") to "0.12.15",
    listOf(
      "org.junit.jupiter:junit-jupiter-api",
      "org.junit.jupiter:junit-jupiter-engine"
    ) to "5.8.1",
  )

  init {
    versions.forEach { (key, value) ->
      key.forEach {
        mapVersion.put(it, value)
      }
    }
  }
}
