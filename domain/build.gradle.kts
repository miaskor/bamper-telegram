dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-jooq")
  implementation("org.springframework.boot:spring-boot-starter-data-rest")
  implementation("org.liquibase:liquibase-core")
  implementation("org.jooq:jooq-meta:3.15.1")
  implementation("org.jooq:jooq-codegen:3.15.1")
  jooqGenerator("mysql:mysql-connector-java:8.0.18")

  runtimeOnly("mysql:mysql-connector-java:8.0.18")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}



jooq {
  version.set("3.15.1")  // default (can be omitted)

  configurations {
    create("main") {  // name of the jOOQ configuration
      jooqConfiguration.apply {
        jdbc.apply {
          driver = "com.mysql.cj.jdbc.Driver"
          url = "jdbc:mysql://localhost:3306/bamper_db"
          user = "miaskor"
          password = "Goblin2001"
        }
        generator.apply {
          name = "org.jooq.codegen.KotlinGenerator"
          database.apply {
            name = "org.jooq.meta.mysql.MySQLDatabase"
            inputSchema = "bamper_db"
            includes = "brand|car|advertisement"
          }
          generate.apply {
            isDeprecated = false
            isRecords = true
            isImmutablePojos = true
            isFluentSetters = false
          }
          target.apply {
            packageName = "by.miaskor.domain"
            directory = "build/generated-src/jooq/main"  // default (can be omitted)
          }
          strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
        }
      }
    }
  }
}

